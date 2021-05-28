package com.foryouandme.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.R
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.action.openApp
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<MainStateUpdate>,
    private val loadingFlow: LoadingFlow<MainLoading>,
    private val errorFlow: ErrorFlow<MainError>,
    private val navigationFlow: NavigationFlow,
    private val navigator: Navigator,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = MainState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- configuration --- */

    private suspend fun getConfiguration() {

        loadingFlow.show(MainLoading.Config)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        //state = state.copy(configuration = configuration)
        stateUpdateFlow.update(MainStateUpdate.Config(configuration))

        loadingFlow.hide(MainLoading.Config)

    }

    /* --- page --- */

    private suspend fun setRestorePage(id: Int) {

        state = state.copy(restorePage = id)
        stateUpdateFlow.update(MainStateUpdate.RestorePage(id))

    }

    private suspend fun selectFeed(): Unit =
        stateUpdateFlow.update(MainStateUpdate.PageNavigation(R.id.feed_navigation))

    private suspend fun selectTasks(): Unit =
        stateUpdateFlow.update(MainStateUpdate.PageNavigation(R.id.tasks_navigation))

    private suspend fun selectYourData(): Unit =
        stateUpdateFlow.update(MainStateUpdate.PageNavigation(R.id.user_data_navigation))

    private suspend fun selectStudyInfo(): Unit =
        stateUpdateFlow.update(MainStateUpdate.PageNavigation(R.id.study_info_navigation))

    fun getPagedIds(): List<Int> =
        listOf(
            R.navigation.feed_navigation,
            R.navigation.tasks_navigation,
            R.navigation.user_data_navigation,
            R.navigation.study_info_navigation
        )


    /* --- deep link --- */

    suspend fun handleDeepLink(fyamState: FYAMState) {

        val taskId =
            fyamState.taskId?.getContentOnce()?.getOrNull()
        val url =
            fyamState.url?.getContentOnce()?.getOrNull()
        val openApplicationIntegration =
            fyamState.openAppIntegration?.getContentOnce()?.getOrNull()

        when {

            taskId != null ->
                navigationFlow.navigateTo(MainToTask(taskId))
            url != null ->
                navigationFlow.navigateTo(AnywhereToWeb(url))
            openApplicationIntegration != null ->
                navigator.performAction(openApp(openApplicationIntegration.packageName))

        }

    }

    /* --- analytics --- */

    private suspend fun logBottomBarPageEvent(itemId: Int) {

        when (itemId) {
            R.id.feed_navigation -> logFeedsViewed()
            R.id.tasks_navigation -> logTasksViewed()
            R.id.user_data_navigation -> logYourDataViewed()
            R.id.study_info_navigation -> logStudyInfoViewed()
        }

    }

    private suspend fun logFeedsViewed() {

        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.Feed,
            EAnalyticsProvider.ALL
        )
        sendAnalyticsEventUseCase(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.Feed),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logTasksViewed() {

        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.Task,
            EAnalyticsProvider.ALL
        )
        sendAnalyticsEventUseCase(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.Task),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logYourDataViewed() {

        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.YourData,
            EAnalyticsProvider.ALL
        )
        sendAnalyticsEventUseCase(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.UserData),
            EAnalyticsProvider.ALL
        )

    }

    private suspend fun logStudyInfoViewed() {

        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.StudyInfo,
            EAnalyticsProvider.ALL
        )
        sendAnalyticsEventUseCase(
            AnalyticsEvent.SwitchTab(AnalyticsEvent.Tab.StudyInfo),
            EAnalyticsProvider.ALL
        )

    }

    /* --- state event --- */

    fun execute(stateEvent: MainStateEvent) {

        when (stateEvent) {
            MainStateEvent.GetConfig ->
                errorFlow.launchCatch(viewModelScope, MainError.Config)
                { getConfiguration() }
            is MainStateEvent.LoadPageSelected ->
                viewModelScope.launchSafe { logBottomBarPageEvent(stateEvent.itemId) }
            is MainStateEvent.SetRestorePage ->
                viewModelScope.launchSafe { setRestorePage(stateEvent.itemId) }
            is MainStateEvent.HandleDeepLink ->
                viewModelScope.launchSafe { handleDeepLink(stateEvent.fyamState) }
            MainStateEvent.SelectFeed ->
                viewModelScope.launchSafe { selectFeed() }
            MainStateEvent.SelectTasks ->
                viewModelScope.launchSafe { selectTasks() }
            MainStateEvent.SelectYourData ->
                viewModelScope.launchSafe { selectYourData() }
            MainStateEvent.SelectStudyInfo ->
                viewModelScope.launchSafe { selectStudyInfo() }
        }

    }

}