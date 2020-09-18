package org.fouryouandme.researchkit.step.video

import arrow.optics.optics
import org.fouryouandme.researchkit.step.Step

@optics
data class VideoDiaryState(
    val step: Step.VideoDiaryStep,
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

sealed class VideoDiaryStateUpdate {

    data class RecordTime(
        val time: Long,
    ) : VideoDiaryStateUpdate()

    data class Recording(val recordingState: RecordingState) : VideoDiaryStateUpdate()

    data class Flash(val isFlashEnabled: Boolean) : VideoDiaryStateUpdate()

    data class Camera(val isBackCameraToggled: Boolean) : VideoDiaryStateUpdate()

}

sealed class RecordingState {

    object Recording : RecordingState()
    object Merged : RecordingState()
    object RecordingPause : RecordingState()
    object Review : RecordingState()
    object ReviewPause : RecordingState()
    object Uploaded : RecordingState()

}

sealed class VideoDiaryError {

    object Recording : VideoDiaryError()
    object Merge : VideoDiaryError()
    object Upload : VideoDiaryError()

}

sealed class VideoDiaryLoading {

    object Merge : VideoDiaryLoading()
    object Upload : VideoDiaryLoading()

}