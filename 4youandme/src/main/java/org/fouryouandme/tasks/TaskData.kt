package org.fouryouandme.tasks

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.Task

data class TaskState(
    val configuration: Configuration,
    val task: Task,
    val isCancelled: Boolean,
    val result: TaskResult
)

sealed class TaskStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val task: Task
    ) : TaskStateUpdate()

    data class Cancelled(val isCancelled: Boolean) : TaskStateUpdate()

}

sealed class TaskLoading {

    object Initialization : TaskLoading()
}

sealed class TaskError {

    object Initialization : TaskError()

}

/* --- navigation --- */

data class StepToStep(val index: Int) : NavigationAction

