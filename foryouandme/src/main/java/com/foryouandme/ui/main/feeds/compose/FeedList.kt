package com.foryouandme.ui.main.feeds.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.notifiable.FeedAction
import com.foryouandme.ui.compose.ForYouAndMeTheme
import com.foryouandme.ui.compose.error.Error
import com.foryouandme.ui.compose.error.ErrorItem
import com.foryouandme.ui.compose.loading.Loading
import com.foryouandme.ui.main.compose.items.*
import com.foryouandme.ui.main.feeds.FeedsState

@Composable
fun FeedList(
    state: FeedsState,
    configuration: Configuration,
    onFirstPageRetry: () -> Unit = {},
    onNextPageRetry: () -> Unit = {},
    onFeedScrollPositionChange: (Int) -> Unit = {},
    onAnswerSelected: (FeedItem.QuickActivityItem, QuickActivityAnswer) -> Unit = { _, _ -> },
    onSubmit: (FeedItem.QuickActivityItem) -> Unit = {},
    onTaskActivityClicked: (FeedItem.TaskActivityItem) -> Unit = {},
    onFeedActionClicked: (FeedAction) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        val lazyListState = rememberLazyListState()
        val feeds = state.items.currentOrPrevious()

        LaunchedEffect(key1 = state.firstPage) {
            if(state.firstPage.isLoading())
                lazyListState.animateScrollToItem(0)
        }

        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            if (feeds != null) {

                item(state.user?.points) {
                    FeedHeader(configuration = configuration, user = state.user)
                }

                itemsIndexed(feeds) { index, item ->
                    onFeedScrollPositionChange(index)
                    when (item) {
                        is FeedItem.TaskActivityItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                TaskActivityItem(
                                    item = item,
                                    configuration = configuration,
                                    onStartClicked = onTaskActivityClicked
                                )
                            }
                        is FeedItem.QuickActivitiesItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                QuickActivitiesItem(
                                    item = item,
                                    configuration = configuration,
                                    onAnswerSelected = onAnswerSelected,
                                    onSubmit = onSubmit
                                )
                            }
                        is FeedItem.DateItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                DateItem(item = item, configuration = configuration)
                            }
                        is FeedItem.FeedRewardItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                FeedRewardItem(
                                    item = item,
                                    configuration = configuration,
                                    onStartClicked = onFeedActionClicked
                                )
                            }
                        is FeedItem.FeedEducationalItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                FeedEducationalItem(
                                    item = item,
                                    configuration = configuration,
                                    onStartClicked = onFeedActionClicked
                                )
                            }
                        is FeedItem.FeedAlertItem ->
                            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                                FeedAlertItem(
                                    item = item,
                                    configuration = configuration,
                                    onStartClicked = onFeedActionClicked
                                )
                            }
                        else -> Unit
                    }
                }

                when (state.items) {
                    is LazyData.Loading ->
                        item {
                            Loading(
                                backgroundColor = Color.Transparent,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            )
                        }
                    is LazyData.Error ->
                        item {
                            ErrorItem(
                                error = state.items.error,
                                configuration = configuration,
                                retry = onNextPageRetry
                            )
                        }
                    else -> {
                    }
                }
            }
        }
        when (state.firstPage) {
            is LazyData.Error ->
                Error(
                    error = state.firstPage.error,
                    configuration = configuration,
                    modifier = Modifier.fillMaxSize(),
                    retry = onFirstPageRetry
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

@Preview
@Composable
private fun TasksListPreview() {
    ForYouAndMeTheme {
        FeedList(
            state = FeedsState.mock(),
            configuration = Configuration.mock()
        )
    }
}