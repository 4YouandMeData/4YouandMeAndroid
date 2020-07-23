package org.fouryouandme.main.tasks

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class TasksState(
    val configuration: Option<Configuration> = None
)

sealed class TasksStateUpdate {
    data class Initialization(val configuration: Configuration) : TasksStateUpdate()
}

sealed class TasksLoading {
    object Initialization : TasksLoading()
}

sealed class TasksError {
    object Initialization : TasksError()
}