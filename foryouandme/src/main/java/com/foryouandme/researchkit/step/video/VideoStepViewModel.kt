package com.foryouandme.researchkit.step.video

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.entity.camera.CameraEvent
import com.foryouandme.entity.camera.CameraFlash
import com.foryouandme.entity.camera.CameraLens
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
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    @ApplicationContext private val application: Context
) : ViewModel() {

    /* --- state --- */

    val state = MutableStateFlow(VideoState())
    val stateFlow = state as StateFlow<VideoState>

    init {

        getVideoDirectory().deleteRecursively()

    }

    /* --- step --- */

    private suspend fun setStep(step: VideoStep?) {
        state.emit(state.value.copy(step = step))
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
        state.emit(
            state.value.copy(recordTimeSeconds = state.value.recordTimeSeconds + 1)
        )
    }

    /* --- media --- */

    private val cameraEventChannel = Channel<CameraEvent>(Channel.BUFFERED)
    val cameraEvents = cameraEventChannel.receiveAsFlow()

    private suspend fun playPause() {

        when (state.value.recordingState) {
            RecordingState.Recording -> pause()
            RecordingState.RecordingPause -> record()
            RecordingState.Review ->
                state.emit(state.value.copy(recordingState = RecordingState.ReviewPause))
            RecordingState.ReviewPause ->
                state.emit(state.value.copy(recordingState = RecordingState.Review))
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

        pause()

    }

    /* --- pause --- */

    private suspend fun pause() {

        logPauseRecording()

        timer?.cancel()
        state.emit(state.value.copy(recordingState = RecordingState.RecordingPause))
        cameraEventChannel.send(CameraEvent.Pause)
    }

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
        }
    }

}