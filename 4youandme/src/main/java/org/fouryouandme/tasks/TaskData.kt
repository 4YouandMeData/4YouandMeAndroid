package org.fouryouandme.tasks

import arrow.optics.optics
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.Task

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
    object Result : TaskLoading()
}

sealed class TaskError {

    object Initialization : TaskError()
    object Result : TaskError()

}

/* --- navigation --- */

data class StepToStep(val index: Int) : NavigationAction

