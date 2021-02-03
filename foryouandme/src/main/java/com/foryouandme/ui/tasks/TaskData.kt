package com.foryouandme.ui.tasks

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.Task

data class TaskState(
    val task: Task? = null,
    val isCancelled: Boolean = false,
    val isCompleted: Boolean = false,
    val result: TaskResult? = null,
    val configuration: Configuration? = null
)

sealed class TaskStateUpdate {

    data class Initialization(
        val task: Task
    ) : TaskStateUpdate()

    data class Cancelled(val isCancelled: Boolean) : TaskStateUpdate()

    object Rescheduled : TaskStateUpdate()

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

sealed class TaskStateEvent {

    data class Initialize(val id: String, val data: Map<String, String>) : TaskStateEvent()
    object Reschedule : TaskStateEvent()
    object End : TaskStateEvent()
    object Cancel: TaskStateEvent()
    data class NextStep(val currentStepIndex: Int): TaskStateEvent()
    data class SkipToStep(val stepId: String?, val currentStepIndex: Int): TaskStateEvent()
    data class AddResult(val result: StepResult): TaskStateEvent()

}

/* --- navigation --- */

data class StepToStep(val index: Int) : NavigationAction
