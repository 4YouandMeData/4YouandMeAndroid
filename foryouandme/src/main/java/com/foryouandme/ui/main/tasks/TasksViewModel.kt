package com.foryouandme.ui.main.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.*
import com.foryouandme.core.ext.isTerminated
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.startCoroutineAsync
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
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.task.Task
import com.foryouandme.ui.main.items.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.addPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharedFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val errorFlow: ErrorFlow<TasksError>,
    private val loadingFlow: LoadingFlow<TasksLoading>,
    private val stateUpdateFlow: StateUpdateFlow<TasksStateUpdate>,
    private val getTasksUseCase: GetTasksUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val submitQuickActivityAnswer: SubmitQuickActivityAnswer,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state: TasksState = TasksState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- job --- */

    private val pageSize: Int = 20
    private var fetchJob: Job? = null

    /* --- tasks --- */

    private suspend fun getTasks(page: Int) {
        coroutineScope {

            loadingFlow.show(TasksLoading.Tasks(page))

            val tasks = async { getTasksUseCase(Order.Descending, page, pageSize) }
            val configuration = async { getConfigurationUseCase(Policy.LocalFirst) }

            val items = buildItems(page, tasks.await(), configuration.await())

            state = state.copy(tasks = items, configuration = configuration.await())
            stateUpdateFlow.update(TasksStateUpdate.Tasks(items))
            stateUpdateFlow.update(TasksStateUpdate.Config(configuration.await()))

            loadingFlow.hide(TasksLoading.Tasks(page))

        }
    }

    private fun buildItems(
        page: Int,
        tasks: PagedList<Task>,
        configuration: Configuration
    ): PagedList<DroidItem<Any>> {

        val items =
            if (page == 1) tasks.map { it.toItem(configuration) }
            else state.tasks.addPage(tasks.map { it.toItem(configuration) })

        return items.sort(configuration)

    }

    private fun Task.toItem(
        configuration: Configuration
    ): DroidItem<Any> =

        when (activity) {
            is QuickActivity ->
                QuickActivityItem(configuration, activity, null)
            is TaskActivity ->
                TaskActivityItem(configuration, activity, from, to)
        }

    private fun PagedList<DroidItem<Any>>.sort(
        configuration: Configuration
    ): PagedList<DroidItem<Any>> {

        val quickActivitiesItem = data.filterIsInstance<QuickActivitiesItem>().firstOrNull()

        val quickActivitiesItems = data.filterIsInstance<QuickActivityItem>()

        val tasks = data.filterIsInstance<TaskActivityItem>()

        val items = mutableListOf<DroidItem<Any>>()

        val quickActivities =
            if (quickActivitiesItems.isNotEmpty())
                if (quickActivitiesItem == null)
                    QuickActivitiesItem(
                        "quick_activities",
                        configuration,
                        DroidAdapter(
                            QuickActivityViewHolder.factory(
                                { item, answer ->
                                    startCoroutineAsync { selectAnswer(item, answer) }
                                },
                                { item -> execute(TasksStateEvent.SubmitQuickActivityAnswer(item)) }
                            )
                        ).also { it.submitList(quickActivitiesItems.toList()) }
                    )
                else {
                    quickActivitiesItem.quickActivities.submitList(
                        quickActivitiesItem.quickActivities.getItems().plus(quickActivitiesItems)
                    )
                    quickActivitiesItem
                }
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
                    DateItem(
                        configuration, LocalDate.parse(
                            key,
                            DateTimeFormatter.ISO_LOCAL_DATE
                        )
                    )
                )
                items.addAll(value)

            }

        return PagedList(items, page, isCompleted)

    }

    /* --- quick activities --- */

    private suspend fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

        logQuickActivityOptionSelected(item.data.id, answer.id)

        state.tasks.data.map { droidItem ->
            when (droidItem) {
                is QuickActivitiesItem -> {

                    val quickActivities =
                        droidItem.quickActivities.getItems()

                    val updatedActivities =
                        quickActivities.map {

                            when (it) {

                                is QuickActivityItem ->
                                    if (it.data.id == item.data.id)
                                        it.copy(selectedAnswer = answer.id)
                                    else
                                        it

                                else -> it
                            }
                        }

                    droidItem.quickActivities.submitList(updatedActivities)

                    QuickActivitiesItem(
                        droidItem.id,
                        droidItem.configuration,
                        droidItem.quickActivities
                    )

                }
                else -> droidItem
            }
        }

    }

    private suspend fun submitAnswer(item: QuickActivityItem) {

        if (item.selectedAnswer.isNullOrEmpty().not()) {

            loadingFlow.show(TasksLoading.QuickActivityUpload)

            submitQuickActivityAnswer(item.data.id, item.selectedAnswer!!.toInt())
            execute(TasksStateEvent.GetTasks)

            loadingFlow.hide(TasksLoading.QuickActivityUpload)

        }

    }

    /* --- analytics --- */

    private suspend fun logQuickActivityOptionSelected(
        activityId: String,
        optionId: String
    ): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.QuickActivityOptionClicked(activityId, optionId),
            EAnalyticsProvider.ALL
        )


    /* --- state event --- */

    fun execute(stateEvent: TasksStateEvent) {

        when (stateEvent) {
            TasksStateEvent.GetTasks -> {

                if (fetchJob?.isActive == true) {
                    fetchJob?.cancel()
                    fetchJob = null
                }

                fetchJob = errorFlow.launchCatch(
                    viewModelScope,
                    TasksError.Tasks(1)
                )
                { getTasks(1) }

            }
            TasksStateEvent.GetTasksNextPage -> {

                if (state.tasks.isCompleted.not()) {

                    val nextPage = state.tasks.page + 1

                    if (fetchJob == null || fetchJob.isTerminated && nextPage != 1)
                        fetchJob =
                            errorFlow.launchCatch(
                                viewModelScope,
                                TasksError.Tasks(nextPage)
                            )
                            { getTasks(nextPage) }
                }

            }
            is TasksStateEvent.SelectQuickActivityAnswer ->
                viewModelScope.launchSafe {
                    selectAnswer(stateEvent.quickActivity, stateEvent.answer)
                }
            is TasksStateEvent.SubmitQuickActivityAnswer ->
                errorFlow.launchCatch(viewModelScope, TasksError.QuickActivityUpload)
                { submitAnswer(stateEvent.quickActivity) }
        }

    }

}