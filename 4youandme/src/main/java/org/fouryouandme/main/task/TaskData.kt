package org.fouryouandme.main.task

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class TaskState(
    val configuration: Option<Configuration> = None
)

sealed class TaskStateUpdate {
    data class Initialization(val configuration: Configuration) : TaskStateUpdate()
}

sealed class TaskLoading {
    object Initialization : TaskLoading()
}

sealed class TaskError {
    object Initialization : TaskError()
}