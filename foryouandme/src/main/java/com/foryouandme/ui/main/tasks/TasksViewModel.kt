package com.foryouandme.ui.main.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.*
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.task.GetTasksUseCase
import com.foryouandme.domain.usecase.task.SubmitQuickActivityAnswer
import com.foryouandme.entity.activity.QuickActivity
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.task.Task
import com.foryouandme.ui.main.compose.items.FeedItem
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.addPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val submitQuickActivityAnswer: SubmitQuickActivityAnswer,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(TasksState())
    val stateFlow = state as StateFlow<TasksState>

    init {
        execute(TasksAction.GetConfiguration)
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
                state.emit(
                    if (page == 1) state.value.copy(firstPage = LazyData.Loading())
                    else state.value.copy(feeds = state.value.feeds.toLoading())
                )
                val tasks =
                    updateWithPage(
                        page,
                        getTasksUseCase(Order.Descending, page, pageSize)
                    )
                state.emit(
                    state.value.copy(
                        tasks = tasks,
                        feeds = tasks.data.map { it.toItem() }.sort().toData(),
                        firstPage = LazyData.unit()
                    )
                )
            },
            {
                state.emit(
                    if (page == 1) state.value.copy(
                        firstPage = it.toError(),
                        feeds = LazyData.Empty
                    )
                    else state.value.copy(feeds = state.value.feeds.toError(it))
                )
            }
        )

    private fun updateWithPage(
        page: Int,
        tasks: PagedList<Task>,
    ): PagedList<Task> =
        if (page == 1) tasks
        else state.value.tasks.addPage(tasks)

    private fun Task.toItem(): FeedItem =

        when (activity) {
            is QuickActivity ->
                FeedItem.QuickActivityItem(activity, null)
            is TaskActivity ->
                FeedItem.TaskActivityItem(activity, from, to)
        }

    private fun List<FeedItem>.sort(): List<FeedItem> {

        val quickActivitiesItem = filterIsInstance<FeedItem.QuickActivitiesItem>().firstOrNull()
        val quickActivitiesItems = filterIsInstance<FeedItem.QuickActivityItem>()
        val tasks = filterIsInstance<FeedItem.TaskActivityItem>()
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

        tasks
            .sortedByDescending { it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
            .groupBy(
                {
                    it.from.withZoneSameInstant(ZoneId.systemDefault())
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

        val feeds = state.value.feeds.dataOrNull()
        if (feeds != null && (position + 1) >= (feeds.size))
            execute(TasksAction.GetTasksNextPage)

    }

    /* --- quick activities --- */

    private suspend fun selectAnswer(
        item: FeedItem.QuickActivityItem,
        answer: QuickActivityAnswer
    ) {

        viewModelScope.launchSafe { logQuickActivityOptionSelected(item.data.id, answer.id) }

        val feedUpdate =
            state.value.feeds.map { list ->
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

        state.emit(state.value.copy(feeds = feedUpdate))

    }

    private fun submitAnswer(item: FeedItem.QuickActivityItem): Action =
        action(
            {
                if (item.selectedAnswer.isNullOrEmpty().not()) {
                    state.emit(state.value.copy(submit = LazyData.Loading()))
                    submitQuickActivityAnswer(item.data.id, item.selectedAnswer!!.toInt())
                    state.emit(state.value.copy(submit = LazyData.unit()))
                    execute(TasksAction.GetTasksFirstPage)
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

    fun execute(action: TasksAction) {

        when (action) {
            TasksAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            TasksAction.GetTasksFirstPage -> {

                if (fetchJob?.isActive == true) {
                    fetchJob?.cancel()
                    fetchJob = null
                }

                fetchJob = viewModelScope.launchAction(getTasks(1))

            }
            TasksAction.GetTasksNextPage -> {
                val tasks = state.value.tasks
                if (tasks.isCompleted.not()) {

                    val nextPage = tasks.page + 1

                    if (fetchJob == null || fetchJob.isTerminated && nextPage != 1)
                        fetchJob = viewModelScope.launchAction(getTasks(nextPage))
                }

            }
            is TasksAction.SetScrollPosition ->
                viewModelScope.launchSafe { setScrollPosition(action.position) }
            is TasksAction.SelectQuickActivityAnswer ->
                viewModelScope.launchSafe { selectAnswer(action.item, action.answer) }
            is TasksAction.SubmitQuickActivityAnswer ->
                viewModelScope.launchAction(submitAnswer(action.item))
            TasksAction.RetrySubmit ->
                viewModelScope.launchSafe { retrySubmit() }
        }

    }

}