package com.foryouandme.researchkit.step.video

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.*
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.permission.RequestPermissionsUseCase
import com.foryouandme.domain.usecase.task.AttachVideoUseCase
import com.foryouandme.domain.usecase.video.MergeVideosUseCase
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.permission.PermissionResult
import com.foryouandme.ui.compose.camera.CameraEvent
import com.foryouandme.ui.compose.camera.CameraFlash
import com.foryouandme.ui.compose.camera.CameraLens
import com.foryouandme.ui.compose.error.toForYouAndMeException
import com.foryouandme.ui.compose.video.VideoPlayerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.File
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class VideoStepViewModel @Inject constructor(
    private val mergeVideosUseCase: MergeVideosUseCase,
    private val attachVideoUseCase: AttachVideoUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    private val requestPermissionsUseCase: RequestPermissionsUseCase,
    @ApplicationContext private val application: Context
) : ViewModel() {

    /* --- state --- */

    val state = MutableStateFlow(VideoStepState())
    val stateFlow = state as StateFlow<VideoStepState>

    private val videoEventChannel = Channel<VideoStepEvent>(Channel.BUFFERED)
    val videoEvents = videoEventChannel.receiveAsFlow()

    init {

        getVideoDirectory().deleteRecursively()

    }

    /* --- step --- */

    private suspend fun setStep(step: VideoStep?) {
        state.emit(state.value.copy(step = step))
    }

    /* --- permissions --- */

    private var permissionJob: Job? = null

    private suspend fun requestPermissions() {

        val permissions =
            requestPermissionsUseCase(
                listOf(
                    Permission.Camera,
                    Permission.RecordAudio
                )
            )

        val deniedPermission =
            permissions.firstOrNull { it is PermissionResult.Denied }?.permission

        if (deniedPermission != null)
            videoEventChannel.send(VideoStepEvent.MissingPermission(deniedPermission))
        else
            state.emit(state.value.copy(permissionsGranted = true))

    }

    /* --- flash --- */

    private suspend fun setFlash(flash: CameraFlash) {
        state.emit(state.value.copy(cameraFlash = flash))
    }

    /* --- camera --- */

    private suspend fun setCameraLens(cameraLens: CameraLens) {
        state.emit(
            state.value.copy(
                cameraLens = cameraLens,
                // disable the flash when the front camera is toggled
                cameraFlash =
                if (cameraLens is CameraLens.Front) CameraFlash.Off
                else state.value.cameraFlash
            )
        )
    }

    /* --- timer --- */

    private var timer: Job? = null

    private suspend fun resumeTimer() {

        delay(1000)
        incrementRecordTime()
        resumeTimer()

    }

    private suspend fun incrementRecordTime() {

        val nextTime = state.value.recordTimeSeconds + 1

        state.emit(
            state.value.copy(recordTimeSeconds = nextTime)
        )

        if (nextTime >= state.value.maxRecordTimeSeconds) {
            pauseRecording()
            delay(1000)
            execute(VideoStepAction.Merge)
        }

    }

    /* --- media --- */

    private val cameraEventChannel = Channel<CameraEvent>(Channel.BUFFERED)
    val cameraEvents = cameraEventChannel.receiveAsFlow()

    private val videoPlayerEventChannel = Channel<VideoPlayerEvent>(Channel.BUFFERED)
    val videoPlayerEvents = videoPlayerEventChannel.receiveAsFlow()

    private suspend fun playPause() {

        if (state.value.permissionsGranted)
            when (state.value.recordingState) {
                RecordingState.Recording -> pauseRecording()
                RecordingState.RecordingPause -> record()
                RecordingState.Review -> {
                    state.emit(state.value.copy(recordingState = RecordingState.ReviewPause))
                    videoPlayerEventChannel.send(VideoPlayerEvent.Pause)
                }
                RecordingState.ReviewPause -> {
                    state.emit(state.value.copy(recordingState = RecordingState.Review))
                    videoPlayerEventChannel.send(VideoPlayerEvent.Play)
                }
                else -> {
                }
            }

    }

    /* --- record --- */

    private suspend fun record() {

        if (state.value.recordTimeSeconds < state.value.maxRecordTimeSeconds) {

            val file = createVideoFile()

            if (state.value.lastRecordedFilePath == null) logStartRecording()
            else logResumeRecording()

            timer?.cancel()
            timer = viewModelScope.launchSafe { resumeTimer() }
            state.emit(
                state.value.copy(
                    recordingState = RecordingState.Recording,
                    startRecordTimeSeconds = state.value.recordTimeSeconds,
                    lastRecordedFilePath = file.absolutePath
                )
            )
            cameraEventChannel.send(CameraEvent.Record(file))

        }

    }

    private suspend fun handleRecordError() {

        // delete the last file if exist
        val lastRecordedFilePath = state.value.lastRecordedFilePath

        if (lastRecordedFilePath != null) {

            val file = File(lastRecordedFilePath)

            if (file.exists()) file.delete()

        }

        // reset the time and the remove the deleted file path
        state.emit(
            state.value.copy(
                recordTimeSeconds = state.value.startRecordTimeSeconds,
                lastRecordedFilePath = null
            )
        )

        pauseRecording()

    }

    private suspend fun pauseRecording() {

        logPauseRecording()

        timer?.cancel()
        state.emit(state.value.copy(recordingState = RecordingState.RecordingPause))
        cameraEventChannel.send(CameraEvent.Pause)
    }

    /* --- merge --- */

    private fun merge(): Action =
        action(
            {

                state.emit(state.value.copy(mergedVideoPath = LazyData.Loading))

                // disable the flash when the user start the review flow
                if (state.value.cameraLens is CameraLens.Back) setFlash(CameraFlash.Off)

                val mergeDirectory = File(getVideoMergeDirectoryPath())

                if (mergeDirectory.exists().not())
                    mergeDirectory.mkdir()

                mergeVideosUseCase(
                    videosPath = getVideoDirectoryPath(),
                    outputPath = mergeDirectory.absolutePath,
                    outputFileName = getVideoMergeFileName()
                )

                state.emit(
                    state.value.copy(
                        recordingState = RecordingState.ReviewPause,
                        mergedVideoPath = getVideoMergeFilePath().toData()
                    )
                )
            },
            {
                state.emit(state.value.copy(mergedVideoPath = it.toError()))
                videoEventChannel.send(VideoStepEvent.MergeError(it.toForYouAndMeException()))
            }
        )

    /* --- submit --- */

    private fun submit(taskId: String): Action =
        action(
            {
                state.emit(state.value.copy(submit = LazyData.Loading))

                attachVideoUseCase(taskId, getVideoMergeFile())

                state.emit(state.value.copy(submit = LazyData.unit()))
                videoEventChannel.send(VideoStepEvent.Submitted)

            },
            {
                state.emit(state.value.copy(submit = it.toError()))
                videoEventChannel.send(VideoStepEvent.SubmitError(it.toForYouAndMeException()))
            }
        )

    /* --- directory --- */

    private fun createVideoFile(): File {

        val directory = getVideoDirectory()

        // crate also the video directory if not exist
        if (!directory.exists())
            directory.mkdir()

        return File(directory, "${System.currentTimeMillis()}.mp4")

    }

    private fun getVideoDirectory(): File = File(getVideoDirectoryPath())

    private fun getVideoDirectoryPath(): String =
        "${application.applicationContext.filesDir.absolutePath}/video-diary"

    private fun getVideoMergeDirectoryPath(): String =
        "${getVideoDirectoryPath()}/merge"

    private fun getVideoMergeFileName(): String =
        "merged_video_diary.mp4"

    private fun getVideoMergeFilePath(): String =
        "${getVideoMergeDirectoryPath()}/${getVideoMergeFileName()}"

    private fun getVideoMergeFile(): File = File(getVideoMergeFilePath())

/* --- analytics --- */

    private suspend fun logPauseRecording() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.VideoDiaryAction(AnalyticsEvent.RecordingAction.Pause),
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logStartRecording() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.VideoDiaryAction(AnalyticsEvent.RecordingAction.Start),
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logResumeRecording() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.VideoDiaryAction(AnalyticsEvent.RecordingAction.Resume),
            EAnalyticsProvider.ALL
        )
    }

    /* --- actions ---- */

    fun execute(action: VideoStepAction) {
        when (action) {
            is VideoStepAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            VideoStepAction.RequestPermissions -> {
                if (permissionJob.isTerminated)
                    permissionJob = viewModelScope.launchSafe { requestPermissions() }
            }
            VideoStepAction.ToggleCamera ->
                viewModelScope.launchSafe {
                    setCameraLens(
                        when (state.value.cameraLens) {
                            CameraLens.Back -> CameraLens.Front
                            CameraLens.Front -> CameraLens.Back
                        }
                    )
                }
            VideoStepAction.ToggleFlash ->
                viewModelScope.launchSafe {
                    setFlash(
                        when (state.value.cameraFlash) {
                            CameraFlash.Off -> CameraFlash.On
                            CameraFlash.On -> CameraFlash.Off
                        }
                    )
                }
            VideoStepAction.PlayPause ->
                viewModelScope.launchSafe { playPause() }
            VideoStepAction.HandleVideoRecordError ->
                viewModelScope.launchSafe { handleRecordError() }
            VideoStepAction.Merge ->
                viewModelScope.launchAction(merge())
            is VideoStepAction.Submit ->
                viewModelScope.launchAction(submit(action.taskId))
        }
    }

}