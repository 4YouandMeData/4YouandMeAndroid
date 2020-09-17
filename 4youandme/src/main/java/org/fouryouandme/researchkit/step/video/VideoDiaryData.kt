package org.fouryouandme.researchkit.step.video

import arrow.optics.optics
import org.fouryouandme.researchkit.step.Step

@optics
data class VideoDiaryState(
    val step: Step.VideoDiaryStep,
    val recordTimeSeconds: Long,
    val maxRecordTimeSeconds: Long,
    val recordingState: RecordingState = RecordingState.Pause,
) {
    companion object
}

sealed class VideoDiaryStateUpdate {

    data class RecordTime(
        val time: Long,
    ) : VideoDiaryStateUpdate()

    data class Recording(val recordingState: RecordingState) : VideoDiaryStateUpdate()

}

sealed class RecordingState {

    object Recording : RecordingState()
    object Pause : RecordingState()
    object Review : RecordingState()

}