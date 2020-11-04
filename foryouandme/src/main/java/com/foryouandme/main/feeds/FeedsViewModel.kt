package com.foryouandme.main.feeds

import arrow.core.Either
import arrow.core.toT
import arrow.fx.coroutines.parMapN
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.FeedModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getUser
import com.foryouandme.core.cases.feed.FeedUseCase.getFeeds
import com.foryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import com.foryouandme.core.entity.activity.QuickActivity
import com.foryouandme.core.entity.activity.QuickActivityAnswer
import com.foryouandme.core.entity.activity.TaskActivity
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.feed.Feed
import com.foryouandme.core.entity.feed.FeedType
import com.foryouandme.core.entity.notifiable.FeedReward
import com.foryouandme.core.entity.user.PREGNANCY_END_DATE_IDENTIFIER
import com.foryouandme.core.entity.user.User
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.main.MainPageToAboutYouPage
import com.foryouandme.main.items.*
import com.foryouandme.main.tasks.TasksToTask
import com.giacomoparisi.recyclerdroid.core.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.DroidItem
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class FeedsViewModel(
    navigator: Navigator,
    private val feedModule: FeedModule,
    private val taskModule: TaskModule,
    private val authModule: AuthModule
) : BaseViewModel<
        FeedsState,
        FeedsStateUpdate,
        FeedsError,
        FeedsLoading>
    (navigator = navigator) {

    /* --- initialize --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {

        showLoading(FeedsLoading.Initialization)

        val userRequest =
            suspend {
                authModule.getUser(CachePolicy.Network)
                    .handleAuthError(rootNavController, navigator)
            }

        val feedRequest =
            suspend {
                feedModule.getFeeds()
                    .handleAuthError(rootNavController, navigator)
            }

        val (user, feed) =
            parMapN(
                Dispatchers.IO,
                userRequest,
                feedRequest,
                { user, feed ->
                    user toT feed
                })

        feed.fold(
            { setError(it, FeedsError.Initialization) },
            { list ->
                setState(
                    FeedsState(
                        list.toItems(rootNavController, configuration)
                            .addHeader(configuration, user.orNull())
                            .addEmptyItem(configuration),
                        user = user.orNull()
                    )
                )
                { FeedsStateUpdate.Initialization(it.feeds, it.user) }
            }
        )

        hideLoading(FeedsLoading.Initialization)

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
                    is TaskActivityItem ->
                        it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    is FeedRewardItem ->
                        it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    is FeedEducationalItem ->
                        it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    is FeedAlertItem ->
                        it.from.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    else ->
                        ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                }
            }
            .groupBy(
                {
                    when (it) {
                        is TaskActivityItem ->
                            it.from.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        is FeedRewardItem ->
                            it.from.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        is FeedEducationalItem ->
                            it.from.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        is FeedAlertItem ->
                            it.from.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        else ->
                            ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                    }
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

        return items

    }

    private fun List<DroidItem<Any>>.addHeader(
        configuration: Configuration,
        user: User?
    ): List<DroidItem<Any>> =
        listOf(FeedHeaderItem(configuration, "1", user?.points?.toString()))
            .plus(this)

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

            showLoading(FeedsLoading.QuickActivityUpload)
            taskModule.updateQuickActivity(item.data.id, item.selectedAnswer!!.toInt())
                .fold(
                    {
                        setError(it, FeedsError.QuickActivityUpload)
                    },
                    {
                        initialize(rootNavController, configuration)
                    }
                )

            hideLoading(FeedsLoading.QuickActivityUpload)
        }
    }

    private fun List<DroidItem<Any>>.addEmptyItem(
        configuration: Configuration
    ): List<DroidItem<Any>> =
        if (size <= 1) plus(listOf(FeedEmptyItem(configuration)))
        else this

    /* --- date --- */

    suspend fun getPregnancyWeeks(user: User?): Long? =
        user?.getPregnancyEndDate()
            ?.getPregnancyStartDate()
            ?.differenceInWeeksFromNow()

    suspend fun getPregnancyMonths(user: User?): Long? =
        user?.getPregnancyEndDate()
            ?.getPregnancyStartDate()
            ?.differenceInMonthsFromNow()

    private suspend fun User.getPregnancyEndDate(): LocalDate? =
        Either.catch {
            getCustomDataByIdentifier(PREGNANCY_END_DATE_IDENTIFIER)
                ?.value
                ?.let { LocalDate.parse(it) }
        }.orNull()

    private suspend fun LocalDate.getPregnancyStartDate(): LocalDate? =
        minusDays(280)

    private suspend fun LocalDate.differenceInMonthsFromNow(): Long =
        ChronoUnit.MONTHS.between(this, LocalDateTime.now().atZone(ZoneOffset.UTC))

    private suspend fun LocalDate.differenceInWeeksFromNow(): Long =
        ChronoUnit.WEEKS.between(this, LocalDateTime.now().atZone(ZoneOffset.UTC))

    /* --- navigation --- */

    suspend fun executeTasks(rootNavController: RootNavController, task: TaskActivityItem): Unit {
        task.data.activityType?.let {
            navigator.navigateTo(
                rootNavController,
                FeedsToTask(task.data.taskId)
            )
        }
    }

    suspend fun aboutYouPage(navController: RootNavController): Unit =
        navigator.navigateTo(
            navController,
            MainPageToAboutYouPage
        )
}