package com.foryouandme.researchkit.recorder

import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.step.sensor.SensorStep
import com.foryouandme.researchkit.task.Task
import java.io.File

data class RecorderState(
    var startTime: Long = 0,
    val recorderList: List<Recorder> = emptyList(),
    val resultList: List<FileResult> = emptyList(),
    val step: SensorStep,
    val task: Task,
    val output: File
)

sealed class RecorderStateUpdate {

    data class Recording(val stepIdentifier: String) : RecorderStateUpdate()

    data class ResultCollected(
        val stepIdentifier: String,
        val files: List<FileResult>
    ) : RecorderStateUpdate()

    data class Completed(val stepIdentifier: String) : RecorderStateUpdate()

}

sealed class RecorderError {

    data class Recording(val stepIdentifier: String) : RecorderError()

}

sealed class SensorData {

    data class Steps(val steps: Int) : SensorData()

}

