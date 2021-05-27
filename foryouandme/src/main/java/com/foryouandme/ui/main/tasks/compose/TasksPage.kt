package com.foryouandme.ui.main.tasks.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.error.Error
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.topappbar.ForYouAndMeTopAppBar
import com.foryouandme.ui.compose.verticalGradient
import com.foryouandme.ui.main.compose.FeedItem
import com.foryouandme.ui.main.tasks.TasksAction.*
import com.foryouandme.ui.main.tasks.TasksState
import com.foryouandme.ui.main.tasks.TasksViewModel

@Composable
fun TasksPage(
    tasksViewModel: TasksViewModel = viewModel(),
    onFeedButtonClicked: () -> Unit = {},
    onStartClicked: (FeedItem.TaskActivityItem) -> Unit = {}
) {

    val state by tasksViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(configuration = state.configuration) { configuration ->

        LaunchedEffect(key1 = "tasks") {
            tasksViewModel.execute(GetTasksFirstPage)
        }

        TasksPage(
            state = state,
            configuration = configuration,
            onFirstPageRetry = { tasksViewModel.execute(GetTasksFirstPage) },
            onNextPageRetry = { tasksViewModel.execute(GetTasksNextPage) },
            onSubmitRetry = { tasksViewModel.execute(RetrySubmit) },
            onFeedScrollPositionChange = { tasksViewModel.execute(SetScrollPosition(it)) },
            onFeedButtonClicked = onFeedButtonClicked,
            onAnswerSelected =
            { item, answer -> tasksViewModel.execute(SelectQuickActivityAnswer(item, answer)) },
            onSubmit = { tasksViewModel.execute(SubmitQuickActivityAnswer(it)) },
            onStartClicked = onStartClicked
        )
    }

}

@Composable
private fun TasksPage(
    state: TasksState,
    configuration: Configuration,
    onFirstPageRetry: () -> Unit = {},
    onNextPageRetry: () -> Unit = {},
    onSubmitRetry: () -> Unit = {},
    onFeedScrollPositionChange: (Int) -> Unit = {},
    onFeedButtonClicked: () -> Unit = {},
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> },
    onSubmit: (FeedItem.QuickActivityItem) -> Unit = {},
    onStartClicked: (FeedItem.TaskActivityItem) -> Unit = {}
) {

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        ForYouAndMeTopAppBar(
            title = configuration.text.tab.tasksTitle,
            titleColor = configuration.theme.secondaryColor.value,
            modifier = Modifier.background(configuration.theme.verticalGradient)
        )
        Box(modifier = Modifier.fillMaxSize()) {
            if (
                state.firstPage.isLoading().not() &&
                state.feeds.dataOrNull()?.isEmpty() == true
            )
                TaskEmpty(configuration = configuration, onFeedButtonClicked = onFeedButtonClicked)
            else
                TaskList(
                    state = state,
                    configuration = configuration,
                    onFirstPageRetry = onFirstPageRetry,
                    onNextPageRetry = onNextPageRetry,
                    onFeedScrollPositionChange = onFeedScrollPositionChange,
                    onAnswerSelected = onAnswerSelected,
                    onSubmit = onSubmit,
                    onStartClicked = onStartClicked
                )
            when (state.submit) {
                is LazyData.Error ->
                    Error(
                        error = state.submit.error,
                        configuration = configuration,
                        modifier = Modifier.fillMaxSize(),
                        retry = onSubmitRetry
                    )
                is LazyData.Loading ->
                    Loading(
                        configuration = configuration,
                        modifier = Modifier.fillMaxSize()
                    )
                else -> {

                }
            }
        }
    }
}

@Preview
@Composable
private fun TasksPagePreview() {
    ForYouAndMeTheme {
        TasksPage(
            state = TasksState.mock(),
            configuration = Configuration.mock()
        )
    }
}