package com.foryouandme.ui.main.tasks

import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.cancelBoundary
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.task.TaskUseCase.getTasks
import com.foryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.ext.startCoroutineCancellableAsync
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.activity.QuickActivity
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.task.Task
import com.foryouandme.ui.main.items.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.addPage
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class TasksViewModel(
    navigator: Navigator,
    private val taskModule: TaskModule,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        TasksState,
        TasksStateUpdate,
        TasksError,
        TasksLoading>
    (navigator = navigator, TasksState()) {

    private val pageSize: Int = 20

    private var fetchDisposable: Disposable? = null

    /* --- tasks --- */

    suspend fun reloadTasks(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit =
        loadTasks(rootNavController, 1, configuration)

    suspend fun nextPage(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit =
        loadTasks(rootNavController, state().tasks.page + 1, configuration)

    suspend fun loadTasks(
        rootNavController: RootNavController,
        page: Int,
        configuration: Configuration
    ): Unit {

        // the first page has the high priority
        if (page == 1) {

            fetchDisposable?.let { it() }
            fetchDisposable = null

        }

        if (page == 1 || fetchDisposable == null)
            fetchDisposable =
                startCoroutineCancellableAsync {

                    showLoading(TasksLoading.Tasks(page))

                    val response =
                        taskModule.getTasks(
                            Order.Descending,
                            page,
                            pageSize
                        ).map { list ->

                            val tasks =
                                if (page == 1) list.map { task -> task.toItem(configuration) }
                                else state().tasks.addPage(list.map { task ->
                                    task.toItem(
                                        configuration
                                    )
                                })


                            tasks.sort(rootNavController, configuration)


                        }.handleAuthError(rootNavController, navigator)


                    cancelBoundary()

                    response.fold(
                        { setError(it, TasksError.Tasks(page)) },
                        { setTasks(it) }
                    )

                    fetchDisposable = null

                    hideLoading(TasksLoading.Tasks(page))

                }

    }

    private suspend fun setTasks(tasks: PagedList<DroidItem<Any>>): Unit =
        setState(state().copy(tasks = tasks)) { TasksStateUpdate.Tasks(tasks) }

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
        rootNavController: RootNavController,
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
                                { item ->
                                    startCoroutineAsync {
                                        submitAnswer(
                                            item,
                                            rootNavController,
                                            configuration
                                        )
                                    }
                                }
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

    private suspend fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

        logQuickActivityOptionSelected(item.data.id, answer.id)

        state().tasks.data.map { droidItem ->
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

                    evalOnMain { droidItem.quickActivities.submitList(updatedActivities) }

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

    private suspend fun submitAnswer(
        item: QuickActivityItem,
        rootNavController: RootNavController,
        configuration: Configuration
    ) {
        if (item.selectedAnswer.isNullOrEmpty().not()) {

            showLoading(TasksLoading.QuickActivityUpload)
            taskModule.updateQuickActivity(item.data.id, item.selectedAnswer!!.toInt())
                .fold(
                    {
                        setError(it, TasksError.QuickActivityUpload)
                    },
                    {
                        reloadTasks(rootNavController, configuration)
                    }
                )

            hideLoading(TasksLoading.QuickActivityUpload)
        }
    }

/* --- navigation --- */

    suspend fun executeTasks(rootNavController: RootNavController, task: TaskActivityItem): Unit {
        task.data.activityType?.let {
            navigator.navigateToSuspend(
                rootNavController,
                TasksToTask(task.data.taskId)
            )
        }
    }

/* --- analytics --- */

    private suspend fun logQuickActivityOptionSelected(
        activityId: String,
        optionId: String
    ): Unit =
        analyticsModule.logEvent(
            AnalyticsEvent.QuickActivityOptionClicked(activityId, optionId),
            EAnalyticsProvider.ALL
        )

}