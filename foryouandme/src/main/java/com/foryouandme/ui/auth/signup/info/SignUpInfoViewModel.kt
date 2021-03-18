package com.foryouandme.ui.auth.signup.info

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
class SignUpInfoViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<SignUpInfoStateUpdate>,
    private val loadingFlow: LoadingFlow<SignUpInfoLoading>,
    private val errorFlow: ErrorFlow<SignUpInfoError>,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state: SignUpInfoState = SignUpInfoState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error

    /* --- configuration --- */

    private suspend fun getConfiguration() {

        loadingFlow.show(SignUpInfoLoading.Configuration)

        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        state = state.copy(configuration = configuration)
        stateUpdateFlow.update(SignUpInfoStateUpdate.Config(configuration))

        loadingFlow.hide(SignUpInfoLoading.Configuration)

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.RequestSetUp,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: SignUpInfoStateEvent) {
        when (stateEvent) {
            SignUpInfoStateEvent.GetConfiguration ->
                errorFlow.launchCatch(
                    viewModelScope,
                    SignUpInfoError.Configuration,
                    loadingFlow,
                    SignUpInfoLoading.Configuration
                )
                { getConfiguration() }
            SignUpInfoStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }

    }

}