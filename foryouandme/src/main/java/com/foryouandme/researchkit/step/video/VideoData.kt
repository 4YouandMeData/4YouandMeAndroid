package com.foryouandme.researchkit.step.video

import java.io.File

data class VideoState(
    val step: VideoStep? = null,
    val startRecordTimeSeconds: Long = 0,
    val recordTimeSeconds: Long = 0,
    val maxRecordTimeSeconds: Long = 120,
    val lastRecordedFilePath: String? = null,
    val recordingState: RecordingState = RecordingState.RecordingPause,
    val isFlashEnabled: Boolean = false,
    val isBackCameraToggled: Boolean = true,
)

sealed class VideoStateUpdate {

    data class RecordTime(
        val time: Long,
    ) : VideoStateUpdate()

    object Recording : VideoStateUpdate()
    object Flash : VideoStateUpdate()
    object Camera : VideoStateUpdate()

}

sealed class RecordingState {

    object Recording : RecordingState()
    object Merged : RecordingState()
    object RecordingPause : RecordingState()
    object Review : RecordingState()
    object ReviewPause : RecordingState()
    object Uploaded : RecordingState()

}

sealed class VideoError {

    object Recording : VideoError()
    object Merge : VideoError()
    object Upload : VideoError()

}

sealed class VideoLoading {

    object Merge : VideoLoading()
    object Upload : VideoLoading()

}

sealed class VideoStateEvent {

    object ToggleCamera : VideoStateEvent()
    object ToggleFlash : VideoStateEvent()

    data class Merge(
        val videoDirectoryPath: String,
        val mergeDirectory: String,
        val videoMergeFileName: String
    ): VideoStateEvent()

    data class Submit(val taskId: String, val file: File) : VideoStateEvent()
    object HandleRecordError : VideoStateEvent()
    object Pause : VideoStateEvent()
    data class Record(val filePath: String): VideoStateEvent()
    object ReviewPlay: VideoStateEvent()
    object ReviewPause: VideoStateEvent()

}

sealed class VideoStepAction {

    data class SetStep(val step: VideoStep?): VideoStepAction()

    object ToggleCamera : VideoStepAction()
    object ToggleFlash : VideoStepAction()

}