package org.fouryouandme.tasks

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class TaskState(
    val configuration: Option<Configuration> = None,
    val task: Option<Task> = None
)

sealed class TaskStateUpdate {

    data class Initialization(
        val configuration: Configuration,
        val task: Task
    ) : TaskStateUpdate()

}

sealed class TaskLoading {

    object Initialization : TaskLoading()
}

sealed class TaskError {

    object Initialization : TaskError()

}

