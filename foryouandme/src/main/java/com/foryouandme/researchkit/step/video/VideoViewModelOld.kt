package com.foryouandme.researchkit.step.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.task.AttachVideoUseCase
import com.foryouandme.domain.usecase.video.MergeVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VideoViewModelOld @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<VideoStateUpdate>,
    private val loadingFlow: LoadingFlow<VideoLoading>,
    private val errorFlow: ErrorFlow<VideoError>,
    private val attachVideoUseCase: AttachVideoUseCase,
    private val mergeVideosUseCase: MergeVideosUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var stateOld: VideoStepState = VideoStepState()
        private set

    val state = MutableStateFlow(VideoStepState())
    val stateFlow = state as StateFlow<VideoStepState>

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- job --- */

    private var timer: Job? = null

    /* --- state --- */

    private suspend fun record(filePath: String) {

        if (stateOld.lastRecordedFilePath == null) logStartRecording() else logResumeRecording()

        timer?.cancel()
        timer = viewModelScope.launchSafe { resumeTimer() }
        stateOld =
            stateOld.copy(
                recordingState = RecordingState.Recording,
                startRecordTimeSeconds = stateOld.recordTimeSeconds,
                lastRecordedFilePath = filePath
            )
        stateUpdateFlow.update(VideoStateUpdate.Recording)

    }

    private suspend fun pause() {

        logPauseRecording()

        timer?.cancel()
        stateOld = stateOld.copy(recordingState = RecordingState.RecordingPause)
        stateUpdateFlow.update(VideoStateUpdate.Recording)

    }

    private suspend fun resumeTimer() {

        delay(1000)
        incrementRecordTime()
        resumeTimer()

    }

    private suspend fun incrementRecordTime() {
        stateOld = stateOld.copy(recordTimeSeconds = stateOld.recordTimeSeconds + 1)
        stateUpdateFlow.update(VideoStateUpdate.RecordTime(stateOld.recordTimeSeconds))
    }

    private suspend fun handleRecordError() {

        // delete the last file if exist
        stateOld.lastRecordedFilePath?.let {

            val file = File(it)

            if (file.exists()) file.delete()

        }

        // reset the time and the remove the deleted file path
        stateOld =
            stateOld.copy(
                recordTimeSeconds = stateOld.startRecordTimeSeconds,
                lastRecordedFilePath = null
            )

        pause()

    }

    private suspend fun setCamera(isBackCameraToggled: Boolean) {



    }

    private suspend fun setFlash(flash: Boolean) {

    }

    private suspend fun merge(videosPath: String, outputPath: String, outputFileName: String) {

        loadingFlow.show(VideoLoading.Merge)

        // disable the flash when the user start the review flow
        //if (stateOld.isBackCameraToggled) setFlash(false)

        mergeVideosUseCase(videosPath, outputPath, outputFileName)

        stateOld = stateOld.copy(recordingState = RecordingState.Merged)
        stateUpdateFlow.update(VideoStateUpdate.Recording)

        loadingFlow.hide(VideoLoading.Merge)

    }

    private suspend fun reviewPause() {

        stateOld = stateOld.copy(recordingState = RecordingState.ReviewPause)
        stateUpdateFlow.update(VideoStateUpdate.Recording)

    }

    private suspend fun reviewPlay() {

        stateOld = stateOld.copy(recordingState = RecordingState.Review)
        stateUpdateFlow.update(VideoStateUpdate.Recording)

    }

    /* --- submit --- */

    private suspend fun submit(taskId: String, file: File) {

        loadingFlow.show(VideoLoading.Upload)

        attachVideoUseCase(taskId, file)

        stateOld = stateOld.copy(recordingState = RecordingState.Uploaded)
        stateUpdateFlow.update(VideoStateUpdate.Recording)

        loadingFlow.hide(VideoLoading.Upload)

    }

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

    /* --- state event --- */

    fun execute(stateEvent: VideoStateEvent) {

        when (stateEvent) {
            VideoStateEvent.ToggleCamera ->
                viewModelScope.launchSafe {  }
            VideoStateEvent.ToggleFlash ->
                viewModelScope.launchSafe {  }
            is VideoStateEvent.Submit ->
                errorFlow.launchCatch(viewModelScope, VideoError.Upload)
                { submit(stateEvent.taskId, stateEvent.file) }
            is VideoStateEvent.Merge ->
                errorFlow.launchCatch(
                    viewModelScope,
                    VideoError.Merge,
                    loadingFlow,
                    VideoLoading.Merge
                ) {
                    merge(
                        stateEvent.videoDirectoryPath,
                        stateEvent.mergeDirectory,
                        stateEvent.videoMergeFileName
                    )
                }
            VideoStateEvent.HandleRecordError ->
                viewModelScope.launchSafe { handleRecordError() }
            VideoStateEvent.Pause ->
                viewModelScope.launchSafe { pause() }
            is VideoStateEvent.Record ->
                viewModelScope.launchSafe { record(stateEvent.filePath) }
            VideoStateEvent.ReviewPlay ->
                viewModelScope.launchSafe { reviewPlay() }
            VideoStateEvent.ReviewPause ->
                viewModelScope.launchSafe { reviewPause() }
        }

    }

}