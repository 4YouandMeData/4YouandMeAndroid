package com.foryouandme.researchkit.task

import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.step.Step

abstract class TaskConfiguration {

    abstract suspend fun build(id: String, data: Map<String, String>): Task?

    abstract suspend fun handleTaskResult(
        result: TaskResult,
        type: String,
        id: String
    )

    abstract suspend fun reschedule(id: String)

    open suspend fun onStepLoaded(task: Task, step: Step) {

    }

}

