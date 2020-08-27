package org.fouryouandme.researchkit.recorder

import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import java.io.File

data class RecorderState(
     var startTime: Long = 0,
     val recorderList: List<Recorder> = emptyList(),
     val resultList: List<FileResult> = emptyList(),
     val step: Step.ActiveStep,
     val task: Task,
     val taskResult: TaskResult,
     val output: File
)