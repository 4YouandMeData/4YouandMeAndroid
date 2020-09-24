package org.fouryouandme.researchkit.recorder

import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import java.io.File

data class RecorderState(
    var startTime: Long = 0,
    val recorderList: List<Recorder> = emptyList(),
    val resultList: List<FileResult> = emptyList(),
    val step: Step.SensorStep,
    val task: Task,
    val output: File
)

sealed class RecordingState {

    data class Recording(val stepIdentifier: String) : RecordingState()

    data class ResultCollected(
        val stepIdentifier: String,
        val files: List<FileResult>
    ) : RecordingState()

    data class Completed(val stepIdentifier: String) : RecordingState()
    data class Failure(val stepIdentifier: String) : RecordingState()

}

sealed class SensorData {

    data class Steps(val steps: Int) : SensorData()

}

