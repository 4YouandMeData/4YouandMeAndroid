package com.foryouandme.ui.main.tasks

import com.foryouandme.core.arch.navigation.NavigationAction
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

data class TasksState(
    val tasks: PagedList<DroidItem<Any>> = PagedList.empty(),
)

sealed class TasksStateUpdate {
    data class Tasks(
        val tasks: PagedList<DroidItem<Any>>
    ) : TasksStateUpdate()
}

sealed class TasksLoading {
    data class Tasks(val page: Int) : TasksLoading()
    object QuickActivityUpload : TasksLoading()
}

sealed class TasksError {
    data class Tasks(val page: Int) : TasksError()
    object QuickActivityUpload : TasksError()
}

/* --- navigation --- */

data class TasksToTask(
    val id: String,
) : NavigationAction