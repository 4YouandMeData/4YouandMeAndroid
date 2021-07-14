package com.foryouandme.ui.main.feeds.compose

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
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.error.Error
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.compose.statusbar.StatusBar
import com.foryouandme.ui.main.compose.items.FeedItem
import com.foryouandme.ui.main.feeds.FeedsAction
import com.foryouandme.ui.main.feeds.FeedsAction.*
import com.foryouandme.ui.main.feeds.FeedsState
import com.foryouandme.ui.main.feeds.FeedsViewModel

@Composable
fun FeedPage(
    feedsViewModel: FeedsViewModel = viewModel(),
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {},
    onLogoClicked: () -> Unit = {}
) {

    val state by feedsViewModel.stateFlow.collectAsState()

    ForYouAndMeTheme(
        configuration = state.configuration,
        onConfigurationError = { feedsViewModel.execute(GetConfiguration) }
    ) { configuration ->

        LaunchedEffect(key1 = "tasks") {
            feedsViewModel.execute(GetFeedsFirstPage())
        }

        FeedPage(
            state = state,
            configuration = configuration,
            imageConfiguration = feedsViewModel.imageConfiguration,
            onFirstPageRetry = { feedsViewModel.execute(GetFeedsFirstPage()) },
            onNextPageRetry = { feedsViewModel.execute(GetFeedsNextPage) },
            onSubmitRetry = { feedsViewModel.execute(RetrySubmit) },
            onFeedScrollPositionChange = { feedsViewModel.execute(SetScrollPosition(it)) },
            onAnswerSelected =
            { item, answer -> feedsViewModel.execute(SelectQuickActivityAnswer(item, answer)) },
            onSubmit = { feedsViewModel.execute(SubmitQuickActivityAnswer(it)) },
            onTaskActivityClicked = onTaskActivityClicked,
            onFeedActionClicked = onFeedActionClicked,
            onLogoClicked = onLogoClicked,
            onRefresh = { feedsViewModel.execute(GetFeedsFirstPage(true)) }
        )
    }

}

@Composable
private fun FeedPage(
    state: FeedsState,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    onFirstPageRetry: () -> Unit = {},
    onNextPageRetry: () -> Unit = {},
    onSubmitRetry: () -> Unit = {},
    onFeedScrollPositionChange: (Int) -> Unit = {},
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> },
    onSubmit: (FeedItem.QuickActivityItem) -> Unit = {},
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {},
    onLogoClicked: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {

    StatusBar(color = configuration.theme.primaryColorStart.value)
    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(configuration.theme.secondaryColor.value)
    ) {
        FeedTopAppBar(
            configuration = configuration,
            imageConfiguration = imageConfiguration,
            user = state.user,
            onLogoClicked = onLogoClicked
        )
        Box(modifier = Modifier.fillMaxSize()) {
            if (
                state.firstPage.isLoading().not() &&
                state.items.dataOrNull()?.isEmpty() == true
            )
                FeedEmpty(configuration = configuration)
            else
                FeedList(
                    state = state,
                    configuration = configuration,
                    onFirstPageRetry = onFirstPageRetry,
                    onNextPageRetry = onNextPageRetry,
                    onFeedScrollPositionChange = onFeedScrollPositionChange,
                    onAnswerSelected = onAnswerSelected,
                    onSubmit = onSubmit,
                    onTaskActivityClicked = onTaskActivityClicked,
                    onFeedActionClicked = onFeedActionClicked,
                    onRefresh = onRefresh
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
private fun FeedPagePreview() {
    ForYouAndMeTheme {
        FeedPage(
            state = FeedsState.mock(),
            configuration = Configuration.mock(),
            imageConfiguration = ImageConfiguration.mock()
        )
    }
}