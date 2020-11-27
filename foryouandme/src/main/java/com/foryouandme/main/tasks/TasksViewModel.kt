package com.foryouandme.main.tasks

import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.task.TaskUseCase.getTasks
import com.foryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import com.foryouandme.core.entity.activity.QuickActivity
import com.foryouandme.core.entity.activity.QuickActivityAnswer
import com.foryouandme.core.entity.activity.TaskActivity
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.task.Task
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.main.items.*
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.threeten.bp.LocalDate
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
    (navigator = navigator) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoading(TasksLoading.Initialization)

        taskModule.getTasks()
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setError(it, TasksError.Initialization) },
                { list ->
                    setState(TasksState(list.toItems(rootNavController, configuration)))
                    { TasksStateUpdate.Initialization(it.tasks) }
                }
            )

        hideLoading(TasksLoading.Initialization)

    }

    private fun List<Task>.toItems(
        rootNavController: RootNavController,
        configuration: Configuration
    ): List<DroidItem<Any>> {

        val quickActivities = mutableListOf<QuickActivityItem>()

        val taskActivities = mutableListOf<TaskActivityItem>()

        forEach {

            when (it.activity) {
                is QuickActivity ->
                    quickActivities.add(
                        QuickActivityItem(configuration, it.activity, null)
                    )
                is TaskActivity ->
                    taskActivities.add(
                        TaskActivityItem(configuration, it.activity, it.from, it.to)
                    )
            }

        }

        val items = mutableListOf<DroidItem<Any>>()

        if (quickActivities.isNotEmpty())
            items.add(
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
                    ).also { it.submitList(quickActivities.toList()) }
                )
            )

        taskActivities
            .sortedByDescending { it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) }
            .groupBy(
                { it.from.format(DateTimeFormatter.ISO_LOCAL_DATE) },
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

        return items

    }

    private suspend fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

        logQuickActivityOptionSelected(item.data.id, answer.id)

        state().tasks.map { droidItem ->
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
                        initialize(rootNavController, configuration)
                    }
                )

            hideLoading(TasksLoading.QuickActivityUpload)
        }
    }

    /* --- navigation --- */

    suspend fun executeTasks(rootNavController: RootNavController, task: TaskActivityItem): Unit {
        task.data.activityType?.let {
            navigator.navigateTo(
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