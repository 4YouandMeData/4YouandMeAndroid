package com.foryouandme.ui.auth.signup.later

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
class SignUpLaterViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<SignUpLaterStateUpdate>,
    private val loadingFlow: LoadingFlow<SignUpLaterLoading>,
    private val errorFlow: ErrorFlow<SignUpLaterError>,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state: SignUpLaterState = SignUpLaterState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- configuration --- */

    private suspend fun getConfiguration() {

        loadingFlow.show(SignUpLaterLoading.Configuration)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        state = state.copy(configuration = configuration)
        stateUpdateFlow.update(SignUpLaterStateUpdate.Config(configuration))

        loadingFlow.hide(SignUpLaterLoading.Configuration)

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.SetupLater,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: SignUpLaterStateEvent) {
        when (stateEvent) {
            SignUpLaterStateEvent.GetConfiguration ->
                errorFlow.launchCatch(
                    viewModelScope,
                    SignUpLaterError.Configuration,
                    loadingFlow,
                    SignUpLaterLoading.Configuration
                )
                { getConfiguration() }
            SignUpLaterStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }

    }

}