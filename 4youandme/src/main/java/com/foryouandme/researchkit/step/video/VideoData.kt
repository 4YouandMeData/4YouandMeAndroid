package com.foryouandme.researchkit.step.video

import arrow.optics.optics

@optics
data class VideoDiaryState(
    val step: VideoStep,
    val startRecordTimeSeconds: Long,
    val recordTimeSeconds: Long,
    val maxRecordTimeSeconds: Long,
    val lastRecordedFilePath: String?,
    val recordingState: RecordingState = RecordingState.RecordingPause,
    val isFlashEnabled: Boolean,
    val isBackCameraToggled: Boolean,
) {
    companion object
}

sealed class VideoStateUpdate {

    data class RecordTime(
        val time: Long,
    ) : VideoStateUpdate()

    data class Recording(val recordingState: RecordingState) : VideoStateUpdate()

    data class Flash(val isFlashEnabled: Boolean) : VideoStateUpdate()

    data class Camera(val isBackCameraToggled: Boolean) : VideoStateUpdate()

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