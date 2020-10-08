package org.fouryouandme.main.feeds

import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.FeedModule
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.feed.FeedUseCase.getFeeds
import org.fouryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import org.fouryouandme.core.entity.activity.QuickActivity
import org.fouryouandme.core.entity.activity.QuickActivityAnswer
import org.fouryouandme.core.entity.activity.TaskActivity
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.task.Task
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.main.MainPageToAboutYouPage
import org.fouryouandme.main.items.*
import org.fouryouandme.main.tasks.TasksToTask
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

class FeedsViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val feedModule: FeedModule,
    private val taskModule: TaskModule
) : BaseViewModel<
        ForIO,
        FeedsState,
        FeedsStateUpdate,
        FeedsError,
        FeedsLoading>
    (navigator = navigator, runtime = runtime) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoadingFx(FeedsLoading.Initialization)

        feedModule.getFeeds()
            .handleAuthError(rootNavController, navigator)
            .fold(
                { setErrorFx(it, FeedsError.Initialization) },
                { list ->
                    setStateFx(
                        FeedsState(
                            list.toItems(configuration)
                                .addHeader(configuration)
                                .addEmptyItem(configuration)
                        )
                    )
                    { FeedsStateUpdate.Initialization(it.feeds) }
                }
            )

        hideLoadingFx(FeedsLoading.Initialization)

    }

    private fun List<Task>.toItems(configuration: Configuration): List<DroidItem<Any>> {

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
                                startCoroutineAsync { submitAnswer(item) }
                            }
                        )
                    ).also { it.submitList(quickActivities.toList()) }
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

    private fun List<DroidItem<Any>>.addHeader(configuration: Configuration): List<DroidItem<Any>> =
        listOf(FeedHeaderItem(configuration, "1", "1000")).plus(this)

    private fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

        state().feeds.map { droidItem ->
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

            taskModule.updateQuickActivity(item.data.id, item.selectedAnswer!!.toInt())
        }
    }

    private fun List<DroidItem<Any>>.addEmptyItem(
        configuration: Configuration
    ): List<DroidItem<Any>> =
        if (size <= 1) plus(listOf(FeedEmptyItem(configuration)))
        else this

    /* --- navigation --- */

    suspend fun executeTasks(rootNavController: RootNavController, task: TaskActivityItem): Unit {
        task.data.activityType?.let {
            navigator.navigateTo(
                rootNavController, TasksToTask(
                    it.type,
                    task.data.id
                )
            )
        }
    }

    suspend fun aboutYouPage(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToAboutYouPage
        )
}