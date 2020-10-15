package org.fouryouandme.main.tasks

import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.task.TaskUseCase.getTasks
import org.fouryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import org.fouryouandme.core.entity.activity.QuickActivity
import org.fouryouandme.core.entity.activity.QuickActivityAnswer
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.main.items.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class TasksViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val taskModule: TaskModule
) : BaseViewModel<
        ForIO,
        TasksState,
        TasksStateUpdate,
        TasksError,
        TasksLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoadingFx(TasksLoading.Initialization)

        taskModule.getTasks()
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setErrorFx(it, TasksError.Initialization) },
                { list ->
                    setStateFx(TasksState(list.toItems(rootNavController, configuration)))
                    { TasksStateUpdate.Initialization(it.tasks) }
                }
            )

        hideLoadingFx(TasksLoading.Initialization)

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
                            { item, answer -> selectAnswer(item, answer) },
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
                { it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME) },
                { it }
            ).forEach { (key, value) ->

                items.add(
                    DateItem(
                        configuration, ZonedDateTime.parse(
                            key,
                            DateTimeFormatter.ISO_ZONED_DATE_TIME
                        )
                    )
                )
                items.addAll(value)

            }

        return items

    }

    private fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

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

    private suspend fun submitAnswer(
        item: QuickActivityItem,
        rootNavController: RootNavController,
        configuration: Configuration
    ) {
        if (item.selectedAnswer.isNullOrEmpty().not()) {

            showLoadingFx(TasksLoading.QuickActivityUpload)
            taskModule.updateQuickActivity(item.data.id, item.selectedAnswer!!.toInt())
                .fold(
                    {
                        setErrorFx(it, TasksError.QuickActivityUpload)
                    },
                    {
                        initialize(rootNavController, configuration)
                    }
                )

            hideLoadingFx(TasksLoading.QuickActivityUpload)
        }
    }

    /* --- navigation --- */

    suspend fun executeTasks(rootNavController: RootNavController, task: TaskActivityItem): Unit {
        task.data.activityType?.let {
            navigator.navigateTo(
                rootNavController,
                TasksToTask(it.type, task.data.taskId, task.data.activityId)
            )
        }
    }
}