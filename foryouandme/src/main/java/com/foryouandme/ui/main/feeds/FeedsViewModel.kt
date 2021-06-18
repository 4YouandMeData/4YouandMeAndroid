package com.foryouandme.ui.main.feeds

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.*
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.feed
.GetFeedUseCase
import com.foryouandme.domain.usecase.task.SubmitQuickActivityAnswer
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.entity.activity.QuickActivity
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.feed.Feed
import com.foryouandme.entity.feed.FeedType
import com.foryouandme.entity.notifiable.FeedAlert
import com.foryouandme.entity.notifiable.FeedEducational
import com.foryouandme.entity.notifiable.FeedReward
import com.foryouandme.ui.main.compose.items.FeedItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.addPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class
FeedsViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val submitQuickActivityAnswer: SubmitQuickActivityAnswer,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(FeedsState())
    val stateFlow = state as StateFlow<FeedsState>

    init {
        execute(FeedsAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = LazyData.Loading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- tasks --- */

    private val pageSize: Int = 20
    private var fetchJob: Job? = null

    private fun getTasks(page: Int): Action =
        action(
            {
                coroutineScope {
                    state.emit(
                        if (page == 1) state.value.copy(firstPage = LazyData.Loading())
                        else state.value.copy(items = state.value.items.toLoading())
                    )

                    val user =
                        async {
                            if (page == 1) getUserUseCase(Policy.Network)
                            else state.value.user
                        }

                    val feeds =
                        async {
                            updateWithPage(
                                page,
                                getFeedUseCase(page, pageSize)
                            )
                        }
                    state.emit(
                        state.value.copy(
                            feeds = feeds.await(),
                            items = feeds.await().data.map { it.toItem() }.sort().toData(),
                            user = user.await(),
                            firstPage = LazyData.unit()
                        )
                    )
                }
            },
            {
                state.emit(
                    if (page == 1) state.value.copy(
                        firstPage = it.toError(),
                        items = LazyData.Empty
                    )
                    else state.value.copy(items = state.value.items.toError(it))
                )
            }
        )

    private fun updateWithPage(
        page: Int,
        feeds: PagedList<Feed>,
    ): PagedList<Feed> =
        if (page == 1) feeds
        else state.value.feeds.addPage(feeds)

    private fun Feed.toItem(): FeedItem =
        when (type) {
            is FeedType.StudyActivityFeed ->
                when (type.studyActivity) {
                    is QuickActivity ->
                        FeedItem.QuickActivityItem(type.studyActivity, null)
                    is TaskActivity ->
                        FeedItem.TaskActivityItem(type.studyActivity, from, to)
                }
            is FeedType.StudyNotifiableFeed ->
                when (type.studyNotifiable) {
                    is FeedAlert ->
                        FeedItem.FeedAlertItem(type.studyNotifiable, from, to)
                    is FeedEducational ->
                        FeedItem.FeedEducationalItem(type.studyNotifiable, from, to)
                    is FeedReward ->
                        FeedItem.FeedRewardItem(type.studyNotifiable, from, to)
                }
        }

    private fun List<FeedItem>.sort(): List<FeedItem> {

        val quickActivitiesItem = filterIsInstance<FeedItem.QuickActivitiesItem>().firstOrNull()
        val quickActivitiesItems = filterIsInstance<FeedItem.QuickActivityItem>()
        val feeds = filter {
            it is FeedItem.TaskActivityItem ||
                    it is FeedItem.FeedRewardItem ||
                    it is FeedItem.FeedAlertItem ||
                    it is FeedItem.FeedEducationalItem
        }
        val items = mutableListOf<FeedItem>()

        val quickActivities =
            if (quickActivitiesItems.isNotEmpty())
                quickActivitiesItem?.copy(
                    items = quickActivitiesItems,
                    selectedIndex =
                    if (quickActivitiesItem.items.size == quickActivitiesItems.size)
                        quickActivitiesItem.selectedIndex
                    else 0
                ) ?: FeedItem.QuickActivitiesItem(quickActivitiesItems.toList(), selectedIndex = 0)
            else quickActivitiesItem

        quickActivities?.let { items.add(it) }

        feeds
            .sortedByDescending {
                val date =
                    when (it) {
                        is FeedItem.FeedAlertItem -> it.from
                        is FeedItem.FeedEducationalItem -> it.from
                        is FeedItem.FeedRewardItem -> it.from
                        is FeedItem.TaskActivityItem -> it.from
                        else -> ZonedDateTime.now()
                    }
                date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

            }
            .groupBy(
                {
                    val date =
                        when (it) {
                            is FeedItem.FeedAlertItem -> it.from
                            is FeedItem.FeedEducationalItem -> it.from
                            is FeedItem.FeedRewardItem -> it.from
                            is FeedItem.TaskActivityItem -> it.from
                            else -> ZonedDateTime.now()
                        }

                    date.withZoneSameInstant(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ISO_LOCAL_DATE)
                },
                { it }
            ).forEach { (key, value) ->

                items.add(
                    FeedItem.DateItem(
                        LocalDate.parse(
                            key,
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                    )
                )
                items.addAll(value)

            }

        return items

    }

    /* --- pagination --- */

    private fun setScrollPosition(position: Int) {

        val items = state.value.items.dataOrNull()
        if (items != null && (position + 1) >= (items.size))
            execute(FeedsAction.GetFeedsNextPage)

    }

    /* --- quick activities --- */

    private suspend fun selectAnswer(
        item: FeedItem.QuickActivityItem,
        answer: QuickActivityAnswer
    ) {

        viewModelScope.launchSafe { logQuickActivityOptionSelected(item.data.id, answer.id) }

        val itemsUpdate =
            state.value.items.map { list ->
                list.map { feed ->
                    when (feed) {
                        is FeedItem.QuickActivitiesItem -> {
                            val items =
                                feed.items
                                    .map {
                                        if (it.data.id == item.data.id)
                                            it.copy(selectedAnswer = answer.id)
                                        else
                                            it
                                    }
                            feed.copy(items = items)
                        }
                        else -> feed
                    }
                }
            }

        state.emit(state.value.copy(items = itemsUpdate))

    }

    private fun submitAnswer(item: FeedItem.QuickActivityItem): Action =
        action(
            {
                if (item.selectedAnswer.isNullOrEmpty().not()) {
                    state.emit(state.value.copy(submit = LazyData.Loading()))
                    submitQuickActivityAnswer(item.data.id, item.selectedAnswer!!.toInt())
                    state.emit(state.value.copy(submit = LazyData.unit()))
                    execute(FeedsAction.GetFeedsFirstPage)
                }
            },
            { state.emit(state.value.copy(submit = it.toError())) }
        )

    private suspend fun retrySubmit() {
        state.emit(state.value.copy(submit = LazyData.unit()))
    }

    /* --- analytics --- */

    private suspend fun logQuickActivityOptionSelected(
        activityId: String,
        optionId: String
    ) {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.QuickActivityOptionClicked(activityId, optionId),
            EAnalyticsProvider.ALL
        )
    }

    /* --- actions --- */

    fun execute(action: FeedsAction) {

        when (action) {
            FeedsAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            FeedsAction.GetFeedsFirstPage -> {

                if (fetchJob?.isActive == true) {
                    fetchJob?.cancel()
                    fetchJob = null
                }

                fetchJob = viewModelScope.launchAction(getTasks(1))

            }
            FeedsAction.GetFeedsNextPage -> {
                val feeds = state.value.feeds
                if (feeds.isCompleted.not()) {

                    val nextPage = feeds.page + 1

                    if (fetchJob == null || fetchJob.isTerminated && nextPage != 1)
                        fetchJob = viewModelScope.launchAction(getTasks(nextPage))
                }

            }
            is FeedsAction.SetScrollPosition ->
                viewModelScope.launchSafe { setScrollPosition(action.position) }
            is FeedsAction.SelectQuickActivityAnswer ->
                viewModelScope.launchSafe { selectAnswer(action.item, action.answer) }
            is FeedsAction.SubmitQuickActivityAnswer ->
                viewModelScope.launchAction(submitAnswer(action.item))
            FeedsAction.RetrySubmit ->
                viewModelScope.launchSafe { retrySubmit() }
        }

    }
}