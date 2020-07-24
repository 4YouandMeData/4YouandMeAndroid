package org.fouryouandme.main.tasks

import arrow.core.None
import arrow.core.Option
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.entity.configuration.Configuration

data class TasksState(
    val configuration: Option<Configuration> = None,
    val tasks: List<DroidItem> = emptyList()
)

sealed class TasksStateUpdate {
    data class Initialization(
        val configuration: Configuration,
        val tasks: List<DroidItem>
    ) : TasksStateUpdate()
}

sealed class TasksLoading {
    object Initialization : TasksLoading()
}

sealed class TasksError {
    object Initialization : TasksError()
}