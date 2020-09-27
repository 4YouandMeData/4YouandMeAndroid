package org.fouryouandme.main.tasks

import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction

data class TasksState(
    val tasks: List<DroidItem<Any>> = emptyList()
)

sealed class TasksStateUpdate {
    data class Initialization(
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

data class TasksToTask(val type: String, val id: String) : NavigationAction