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
import org.fouryouandme.core.entity.feed.Feed
import org.fouryouandme.core.entity.feed.FeedType
import org.fouryouandme.core.entity.notifiable.FeedReward
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
                            list.toItems(rootNavController, configuration)
                                .addHeader(configuration)
                                .addEmptyItem(configuration)
                        )
                    )
                    { FeedsStateUpdate.Initialization(it.feeds) }
                }
            )

        hideLoadingFx(FeedsLoading.Initialization)

    }

    private fun List<Feed>.toItems(
        rootNavController: RootNavController,
        configuration: Configuration
    ): List<DroidItem<Any>> {

        val quickActivities =
            mapNotNull {
                when (it.type) {
                    is FeedType.StudyActivityFeed -> {
                        when (it.type.studyActivity) {
                            is QuickActivity ->
                                QuickActivityItem(
                                    configuration,
                                    it.type.studyActivity,
                                    null
                                )
                            else -> null
                        }
                    }
                    else -> null
                }
            }

        val feeds =
            mapNotNull {

                when (it.type) {
                    is FeedType.StudyActivityFeed ->
                        when (it.type.studyActivity) {
                            is TaskActivity ->
                                TaskActivityItem(
                                    configuration,
                                    it.type.studyActivity,
                                    it.from,
                                    it.to
                                )
                            else -> null
                        }
                    is FeedType.StudyNotifiableFeed ->
                        when (it.type.studyNotifiable) {
                            is FeedReward ->
                                FeedRewardItem(
                                    configuration,
                                    it.type.studyNotifiable,
                                    it.from,
                                    it.to
                                )
                            else -> null
                        }
                    else -> null
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

        feeds
            .sortedByDescending {
                when (it) {
                    is TaskActivityItem -> it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    is FeedRewardItem -> it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    else -> ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                }
            }
            .groupBy(
                {
                    when (it) {
                        is TaskActivityItem -> it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                        is FeedRewardItem -> it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                        else -> ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    }
                },
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

    private suspend fun submitAnswer(
        item: QuickActivityItem,
        rootNavController: RootNavController,
        configuration: Configuration
    ) {
        if (item.selectedAnswer.isNullOrEmpty().not()) {

            showLoadingFx(FeedsLoading.QuickActivityUpload)
            taskModule.updateQuickActivity(item.data.id, item.selectedAnswer!!.toInt())
                .fold(
                    {
                        setErrorFx(it, FeedsError.QuickActivityUpload)
                    },
                    {
                        initialize(rootNavController, configuration)
                    }
                )

            hideLoadingFx(FeedsLoading.QuickActivityUpload)
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