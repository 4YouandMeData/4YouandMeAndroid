package com.foryouandme.tasks

import arrow.optics.optics
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.Task

@optics
data class TaskState(
    val task: Task,
    val isCancelled: Boolean,
    val isCompleted: Boolean,
    val result: TaskResult
) {
    companion object
}

sealed class TaskStateUpdate {

    data class Initialization(
        val task: Task
    ) : TaskStateUpdate()

    data class Cancelled(val isCancelled: Boolean) : TaskStateUpdate()

    object Completed : TaskStateUpdate()

}

sealed class TaskLoading {

    object Initialization : TaskLoading()
    object Reschedule : TaskLoading()
    object Result : TaskLoading()
}

sealed class TaskError {

    object Initialization : TaskError()
    object Reschedule : TaskError()
    object Result : TaskError()

}

/* --- navigation --- */

data class StepToStep(val index: Int) : NavigationAction

