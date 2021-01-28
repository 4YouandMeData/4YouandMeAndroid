package com.foryouandme.ui.main.tasks

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.main.items.QuickActivityItem
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

data class TasksState(
    val tasks: PagedList<DroidItem<Any>> = PagedList.empty(),
    val configuration: Configuration? = null
)

sealed class TasksStateUpdate {

    data class Tasks(
        val tasks: PagedList<DroidItem<Any>>,
    ) : TasksStateUpdate()

    data class Config(val configuration: Configuration): TasksStateUpdate()

}

sealed class TasksLoading {
    data class Tasks(val page: Int) : TasksLoading()
    object QuickActivityUpload : TasksLoading()
}

sealed class TasksError {
    data class Tasks(val page: Int) : TasksError()
    object QuickActivityUpload : TasksError()
}

sealed class TasksStateEvent {

    object GetTasks : TasksStateEvent()

    object GetTasksNextPage : TasksStateEvent()

    data class SelectQuickActivityAnswer(
        val quickActivity: QuickActivityItem,
        val answer: QuickActivityAnswer
    ) : TasksStateEvent()

    data class SubmitQuickActivityAnswer(val quickActivity: QuickActivityItem) : TasksStateEvent()

}

/* --- navigation --- */

data class TasksToTask(
    val id: String,
) : NavigationAction