package com.foryouandme.ui.main.feeds

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.feed.Feed
import com.foryouandme.entity.task.Task
import com.foryouandme.entity.user.User
import com.foryouandme.ui.main.compose.FeedItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

data class FeedsState(
    val feeds: PagedList<Feed> = PagedList.empty(),
    val items: LazyData<List<FeedItem>> = LazyData.Empty,
    val user: User? = null,
    val firstPage: LazyData<Unit> = LazyData.Empty,
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val submit: LazyData<Unit> = LazyData.Empty
) {

    companion object {

        fun mock(): FeedsState =
            FeedsState(
                feeds =
                PagedList(
                    data = listOf(Feed.mock(), Feed.mock()),
                    page =1,
                    isCompleted = true
                ),
                items =
                listOf(FeedItem.TaskActivityItem.mock(), FeedItem.TaskActivityItem.mock()).toData(),
                user = User.mock(),
                firstPage = LazyData.unit(),
                configuration = Configuration.mock().toData(),
                submit = LazyData.unit()
            )

    }

}

sealed class FeedsAction {

    object GetConfiguration : FeedsAction()

    object GetFeedsFirstPage : FeedsAction()

    object GetFeedsNextPage : FeedsAction()

    data class SetScrollPosition(val position: Int) : FeedsAction()

    data class SelectQuickActivityAnswer(
        val item: FeedItem.QuickActivityItem,
        val answer: QuickActivityAnswer
    ) : FeedsAction()

    data class SubmitQuickActivityAnswer(val item: FeedItem.QuickActivityItem) : FeedsAction()

    object RetrySubmit : FeedsAction()

}

/* --- navigation --- */

data class FeedsToTask(
    val id: String,
) : NavigationAction