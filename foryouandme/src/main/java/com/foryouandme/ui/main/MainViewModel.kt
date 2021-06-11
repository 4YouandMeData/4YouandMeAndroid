package com.foryouandme.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.activity.FYAMState
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.Action
import com.foryouandme.core.ext.action
import com.foryouandme.core.ext.launchAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration,
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(MainState())
    val stateFlow = state as StateFlow<MainState>

    /* --- event --- */

    private val events = MutableSharedFlow<UIEvent<MainEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<MainEvent>>

    init {
        execute(MainAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = LazyData.Loading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- deep link --- */

    private suspend fun handleDeepLink(fyamState: FYAMState) {

        val taskId =
            fyamState.taskId?.getContentOnce()?.getOrNull()
        val url =
            fyamState.url?.getContentOnce()?.getOrNull()
        val openApplicationIntegration =
            fyamState.openAppIntegration?.getContentOnce()?.getOrNull()

        when {

            taskId != null ->
                events.emit(MainEvent.OpenTask(taskId).toUIEvent())
            url != null ->
                events.emit(MainEvent.OpenUrl(url).toUIEvent())
            openApplicationIntegration != null ->
                events.emit(MainEvent.OpenApp(openApplicationIntegration.packageName).toUIEvent())

        }

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed(screen: Screen.HomeScreen) {

        when (screen) {
            Screen.HomeScreen.Feed -> logFeedsViewed()
            Screen.HomeScreen.StudyInfo -> logStudyInfoViewed()
            Screen.HomeScreen.Tasks -> logTasksViewed()
            Screen.HomeScreen.YourData -> logYourDataViewed()
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

    /* --- action --- */

    fun execute(action: MainAction) {
        when (action) {
            MainAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            is MainAction.LogScreenSelected ->
                viewModelScope.launchSafe { logScreenViewed(action.screen) }
            is MainAction.HandleDeepLink ->
                viewModelScope.launchSafe { handleDeepLink(action.fyamState) }
        }
    }

}