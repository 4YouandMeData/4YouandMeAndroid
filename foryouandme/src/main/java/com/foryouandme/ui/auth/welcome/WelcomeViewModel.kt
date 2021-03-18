package com.foryouandme.ui.auth.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<WelcomeStateUpdate>,
    private val loadingFlow: LoadingFlow<WelcomeLoading>,
    private val errorFlow: ErrorFlow<WelcomeError>,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state: WelcomeState = WelcomeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- configuration --- */

    private suspend fun getConfiguration() {

        loadingFlow.show(WelcomeLoading.Configuration)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        state = state.copy(configuration = configuration)
        stateUpdateFlow.update(WelcomeStateUpdate.Config(configuration))

        loadingFlow.hide(WelcomeLoading.Configuration)

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.GetStarted,
            EAnalyticsProvider.ALL
        )
    }


    /* --- state event --- */

    fun execute(stateEvent: WelcomeStateEvent) {
        when (stateEvent) {
            WelcomeStateEvent.GetConfiguration ->
                errorFlow.launchCatch(
                    viewModelScope,
                    WelcomeError.Configuration,
                    loadingFlow,
                    WelcomeLoading.Configuration
                )
                { getConfiguration() }
            WelcomeStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }

    }

}