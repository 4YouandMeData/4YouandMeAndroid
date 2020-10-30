package com.fouryouandme.main.tasks

import com.fouryouandme.core.arch.navigation.NavigationAction
import com.giacomoparisi.recyclerdroid.core.DroidItem

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
    object QuickActivityUpload : TasksLoading()
}

sealed class TasksError {
    object Initialization : TasksError()
    object QuickActivityUpload : TasksError()
}

/* --- navigation --- */

data class TasksToTask(
    val type: String,
    val id: String,
) : NavigationAction