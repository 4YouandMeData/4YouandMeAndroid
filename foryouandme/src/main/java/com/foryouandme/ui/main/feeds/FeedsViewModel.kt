package com.foryouandme.ui.main.feeds

import arrow.core.Either
import arrow.core.toT
import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.cancelBoundary
import arrow.fx.coroutines.parMapN
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.FeedModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.openApp
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthUseCase.getUser
import com.foryouandme.core.cases.feed.FeedUseCase.getFeeds
import com.foryouandme.core.cases.task.TaskUseCase.updateQuickActivity
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.core.ext.startCoroutineCancellableAsync
import com.foryouandme.entity.order.Order
import com.foryouandme.entity.activity.QuickActivity
import com.foryouandme.entity.activity.QuickActivityAnswer
import com.foryouandme.entity.activity.TaskActivity
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.feed.Feed
import com.foryouandme.entity.feed.FeedType
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.notifiable.FeedAlert
import com.foryouandme.entity.notifiable.FeedEducational
import com.foryouandme.entity.notifiable.FeedReward
import com.foryouandme.entity.user.PREGNANCY_END_DATE_IDENTIFIER
import com.foryouandme.entity.user.User
import com.foryouandme.ui.main.MainPageToAboutYouPage
import com.foryouandme.ui.main.MainPageToFaq
import com.foryouandme.ui.main.MainPageToInformation
import com.foryouandme.ui.main.MainPageToReward
import com.foryouandme.ui.main.items.*
import com.giacomoparisi.recyclerdroid.core.DroidItem
import com.giacomoparisi.recyclerdroid.core.adapter.DroidAdapter
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.addPage
import kotlinx.coroutines.Dispatchers
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class FeedsViewModel(
    navigator: Navigator,
    private val feedModule: FeedModule,
    private val taskModule: TaskModule,
    private val authModule: AuthModule,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        FeedsState,
        FeedsStateUpdate,
        FeedsError,
        FeedsLoading>
    (navigator = navigator) {

    private val pageSize: Int = 20

    private var fetchDisposable: Disposable? = null

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
                initializeFeeds(rootNavController, configuration)
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
                        list.addHeader(configuration, user.orNull())
                            .addEmptyItem(configuration),
                        user = user.orNull()
                    )
                )
                { FeedsStateUpdate.Initialization(it.feeds, it.user) }
            }
        )

        hideLoading(FeedsLoading.Initialization)

    }

    suspend fun reloadFeeds(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit =
        loadFeed(rootNavController, 1, configuration)

    suspend fun nextPage(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Unit {
        if (state().feeds.isCompleted.not())
            loadFeed(rootNavController, state().feeds.page + 1, configuration)
    }

    private suspend fun initializeFeeds(
        rootNavController: RootNavController,
        configuration: Configuration
    ): Either<ForYouAndMeError, PagedList<DroidItem<Any>>> {

        fetchDisposable?.let { it() }
        fetchDisposable = null

        return feedModule.getFeeds(
            Order.Descending,
            1,
            pageSize
        ).map { list ->

            val feeds = list.map { feeds -> feeds.toItem(configuration) }

            feeds.sort(rootNavController, configuration)

        }

    }

    private suspend fun loadFeed(
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

                    showLoading(FeedsLoading.Feeds(page))

                    val response =
                        feedModule.getFeeds(
                            Order.Descending,
                            page,
                            pageSize
                        ).map { list ->

                            val feeds =
                                if (page == 1) list.map { feeds -> feeds.toItem(configuration) }
                                else
                                    state().feeds.addPage(
                                        list.map { feed ->
                                            feed.toItem(configuration)
                                        }
                                    )

                            feeds.sort(rootNavController, configuration)


                        }.handleAuthError(rootNavController, navigator)


                    cancelBoundary()

                    response.fold(
                        { setError(it, FeedsError.Feeds(page)) },
                        {

                            val feeds =
                                if (page == 1)
                                    it.addHeader(configuration, state().user)
                                        .addEmptyItem(configuration)
                                else it

                            setFeeds(feeds)

                        }
                    )

                    fetchDisposable = null

                    hideLoading(FeedsLoading.Feeds(page))

                }

    }

    private suspend fun setFeeds(feeds: PagedList<DroidItem<Any>>): Unit =
        setState(state().copy(feeds = feeds)) { FeedsStateUpdate.Feeds(feeds) }

    private fun Feed.toItem(
        configuration: Configuration
    ): DroidItem<Any> =

        when (type) {
            is FeedType.StudyActivityFeed ->
                when (type.studyActivity) {
                    is QuickActivity ->
                        QuickActivityItem(configuration, type.studyActivity, null)
                    is TaskActivity ->
                        TaskActivityItem(configuration, type.studyActivity, from, to)
                }
            is FeedType.StudyNotifiableFeed ->
                when (type.studyNotifiable) {
                    is FeedReward ->
                        FeedRewardItem(
                            configuration,
                            type.studyNotifiable,
                            from,
                            to
                        )
                    is FeedAlert ->
                        FeedAlertItem(
                            configuration,
                            type.studyNotifiable,
                            from,
                            to
                        )
                    is FeedEducational ->
                        FeedEducationalItem(
                            configuration,
                            type.studyNotifiable,
                            from,
                            to
                        )
                }
        }

    private fun PagedList<DroidItem<Any>>.sort(
        rootNavController: RootNavController,
        configuration: Configuration
    ): PagedList<DroidItem<Any>> {

        val feedHeaderItem = data.filterIsInstance<FeedHeaderItem>()

        val quickActivitiesItem = data.filterIsInstance<QuickActivitiesItem>().firstOrNull()

        val quickActivitiesItems = data.filterIsInstance<QuickActivityItem>()

        val feeds =
            data.filter { it is TaskActivityItem || it is FeedRewardItem || it is FeedAlertItem }

        val items = mutableListOf<DroidItem<Any>>()

        items.addAll(feedHeaderItem)

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

        feeds
            .sortedByDescending {

                val date =
                    when (it) {
                        is TaskActivityItem -> it.from
                        is FeedRewardItem -> it.from
                        is FeedAlertItem -> it.from
                        is FeedEducationalItem -> it.from
                        else -> ZonedDateTime.now()
                    }

                date.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

            }
            .groupBy(
                {
                    val date =
                        when (it) {
                            is TaskActivityItem -> it.from
                            is FeedRewardItem -> it.from
                            is FeedAlertItem -> it.from
                            is FeedEducationalItem -> it.from
                            else -> ZonedDateTime.now()
                        }

                    date.withZoneSameInstant(ZoneId.systemDefault())
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

    private fun PagedList<DroidItem<Any>>.addHeader(
        configuration: Configuration,
        user: User?
    ): PagedList<DroidItem<Any>> =
        listOf(FeedHeaderItem(configuration, "1", user?.points?.toString()))
            .plus(data)
            .let { PagedList(it, page, isCompleted) }

    private suspend fun selectAnswer(item: QuickActivityItem, answer: QuickActivityAnswer) {

        logQuickActivityOptionSelected(item.data.id, answer.id)

        state().feeds.data.map { droidItem ->
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

    private fun PagedList<DroidItem<Any>>.addEmptyItem(
        configuration: Configuration
    ): PagedList<DroidItem<Any>> =
        if (size <= 1) PagedList(data.plus(FeedEmptyItem(configuration)), 1, true)
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
            navigator.navigateToSuspend(
                rootNavController,
                FeedsToTask(task.data.taskId)
            )
        }
    }

    suspend fun aboutYouPage(rootNavController: RootNavController): Unit =
        navigator.navigateToSuspend(
            rootNavController,
            MainPageToAboutYouPage
        )

    suspend fun web(rootNavController: RootNavController, url: String): Unit =
        navigator.navigateToSuspend(
            rootNavController,
            AnywhereToWeb(url)
        )

    suspend fun openIntegrationApp(integrationApp: IntegrationApp): Unit =
        navigator.performAction(openApp(integrationApp.packageName))

    suspend fun info(navController: RootNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            MainPageToInformation
        )

    suspend fun reward(navController: RootNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            MainPageToReward
        )

    suspend fun faq(navController: RootNavController): Unit =
        navigator.navigateToSuspend(
            navController,
            MainPageToFaq
        )

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