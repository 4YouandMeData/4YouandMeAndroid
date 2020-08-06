package org.fouryouandme.main.tasks

import arrow.core.None
import arrow.core.toOption
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.cases.task.TaskUseCase
import org.fouryouandme.core.entity.activity.QuickActivity
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.foldToKindEither
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.main.items.*
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class TasksViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        TasksState,
        TasksStateUpdate,
        TasksError,
        TasksLoading>
    (TasksState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(TasksLoading.Initialization)

            val data =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)
                    .bind()
                    .foldToKindEither(runtime.fx) { config ->
                        TaskUseCase.getTasks(runtime).mapResult(runtime.fx) { config to it }
                    }
                    .handleAuthError(runtime, rootNavController, navigator)

            !data.fold(
                { setError(it, TasksError.Initialization) },
                {

                    val tasks = it.second.toItems(it.first)

                    setState(
                        state().copy(
                            configuration = it.first.toOption(),
                            tasks = tasks
                        ),
                        TasksStateUpdate.Initialization(it.first, tasks)
                    )
                }
            )

            !hideLoading(TasksLoading.Initialization)

        }.unsafeRunAsync()

    private fun List<Task>.toItems(configuration: Configuration): List<DroidItem> {

        val quickActivities = mutableListOf<QuickActivityItem>()

        val taskActivities = mutableListOf<TaskActivityItem>()

        forEach {

            when (it.activity) {
                is QuickActivity ->
                    quickActivities.add(
                        QuickActivityItem(configuration, it.activity, None)
                    )
                is TaskActivity ->
                    taskActivities.add(
                        TaskActivityItem(configuration, it.activity, it.from, it.to)
                    )
            }

        }

        val items = mutableListOf<DroidItem>()

        if (quickActivities.isNotEmpty())
            items.add(
                QuickActivitiesItem(
                    "quick_activities",
                    DroidAdapter(QuickActivityViewHolder.factory())
                        .also { it.submitList(quickActivities.toList()) }
                )
            )

        taskActivities.groupBy(
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
}