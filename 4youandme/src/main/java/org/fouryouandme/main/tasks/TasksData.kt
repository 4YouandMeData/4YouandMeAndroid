package org.fouryouandme.main.tasks

import arrow.core.None
import arrow.core.Option
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.task.ETaskType

data class TasksState(
    val configuration: Option<Configuration> = None,
    val tasks: List<DroidItem<Any>> = emptyList()
)

sealed class TasksStateUpdate {
    data class Initialization(
        val configuration: Configuration,
        val tasks: List<DroidItem<Any>>
    ) : TasksStateUpdate()
}

sealed class TasksLoading {
    object Initialization : TasksLoading()
}

sealed class TasksError {
    object Initialization : TasksError()
}

/* --- navigation --- */

data class TasksToTask(val type: ETaskType) : NavigationAction