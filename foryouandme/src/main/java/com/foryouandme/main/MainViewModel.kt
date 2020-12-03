package com.foryouandme.main

import com.foryouandme.R
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.openApp
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider

class MainViewModel(
    navigator: Navigator,
    private val analyticsModule: AnalyticsModule
) : BaseViewModel<
        MainState,
        MainStateUpdate,
        Empty,
        Empty>
    (navigator, MainState()) {

    /* --- page --- */

    suspend fun setRestorePage(id: Int): Unit {

        setState(state().copy(restorePage = id)) { MainStateUpdate.RestorePage(id) }

    }

    suspend fun selectFeed(): Unit =
        setState(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.feed_navigation) }

    suspend fun selectTasks(): Unit =
        setState(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.tasks_navigation) }

    suspend fun selectYourData(): Unit =
        setState(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.user_data_navigation) }

    suspend fun selectStudyInfo(): Unit =
        setState(
            state()
        ) { MainStateUpdate.PageNavigation(R.id.study_info_navigation) }

    fun getPagedIds(): List<Int> =
        listOf(
            R.navigation.feed_navigation,
            R.navigation.tasks_navigation,
            R.navigation.user_data_navigation,
            R.navigation.study_info_navigation
        )


    /* --- deep link --- */

    suspend fun handleDeepLink(rootNavController: RootNavController, fyamState: FYAMState): Unit {

        val taskId =
            fyamState.taskId?.getContentOnce()?.orNull()?.item
        val url =
            fyamState.url?.getContentOnce()?.orNull()?.item
        val openApplicationIntegration =
            fyamState.openAppIntegration?.getContentOnce()?.orNull()?.item

        when {

            taskId != null ->
                navigator.navigateTo(rootNavController, MainToTask(taskId))
            url != null ->
                navigator.navigateTo(rootNavController, AnywhereToWeb(url))
            openApplicationIntegration != null ->
                navigator.performAction(openApp(openApplicationIntegration.packageName))

        }

    }

    /* --- analytics --- */

    suspend fun logBottomBarPageEvent(itemId: Int): Unit {

        when (itemId) {
            R.id.feed_navigation -> logFeedsViewed()
            R.id.tasks_navigation -> logTasksViewed()
            R.id.user_data_navigation -> logYourDataViewed()
            R.id.study_info_navigation -> logStudyInfoViewed()
        }

    }

    private suspend fun logFeedsViewed(): Unit {

        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.Feed, EAnalyticsProvider.ALL)
        analyticsModule.logEvent(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.Feed),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logTasksViewed(): Unit {

        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.Task, EAnalyticsProvider.ALL)
        analyticsModule.logEvent(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.Task),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logYourDataViewed(): Unit {

        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.YourData, EAnalyticsProvider.ALL)
        analyticsModule.logEvent(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.UserData),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logStudyInfoViewed(): Unit {

        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.StudyInfo, EAnalyticsProvider.ALL)
        analyticsModule.logEvent(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.StudyInfo),
            EAnalyticsProvider.ALL
        )

    }

}