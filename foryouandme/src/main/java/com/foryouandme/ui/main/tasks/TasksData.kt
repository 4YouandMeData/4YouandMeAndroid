package com.foryouandme.ui.main.tasks

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.task.Task
import com.foryouandme.ui.main.compose.FeedItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

data class TasksState(
    val tasks: PagedList<Task> = PagedList.empty(),
    val feeds: LazyData<List<FeedItem>> = LazyData.Empty,
    val firstPage: LazyData<Unit> = LazyData.Empty,
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val submit: LazyData<Unit> = LazyData.Empty
) {

    companion object {

        fun mock(): TasksState =
            TasksState(
                tasks =
                PagedList(
                    listOf(Task.mock(), Task.mock()),
                    1,
                    true
                ),
                feeds =
                listOf(FeedItem.TaskActivityItem.mock(), FeedItem.TaskActivityItem.mock()).toData(),
                configuration = Configuration.mock().toData()
            )

    }

}

sealed class TasksAction {

    object GetConfiguration : TasksAction()

    object GetTasksFirstPage : TasksAction()

    object GetTasksNextPage : TasksAction()

    data class SetScrollPosition(val position: Int) : TasksAction()

    data class SelectQuickActivityAnswer(
        val item: FeedItem.QuickActivityItem,
        val answer: QuickActivityAnswer
    ) : TasksAction()

    data class SubmitQuickActivityAnswer(val item: FeedItem.QuickActivityItem) : TasksAction()

    object RetrySubmit : TasksAction()

}

/* --- navigation --- */

data class TasksToTask(
    val id: String,
) : NavigationAction