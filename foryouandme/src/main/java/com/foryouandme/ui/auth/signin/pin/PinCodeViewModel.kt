package com.foryouandme.ui.auth.signin.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.flow.StateUpdateFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.PinLoginUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PinCodeViewModel @Inject constructor(
    private val stateUpdateFlow: StateUpdateFlow<PinCodeStateUpdate>,
    private val loadingFlow: LoadingFlow<PinCodeLoading>,
    private val errorFlow: ErrorFlow<PinCodeError>,
    private val navigationFlow: NavigationFlow,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val pinLoginUseCase: PinLoginUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- state --- */

    var state = PinCodeState()
        private set

    /* --- flow --- */

    val stateUpdate = stateUpdateFlow.stateUpdates
    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- configuration --- */

    private suspend fun getConfiguration() {

        loadingFlow.show(PinCodeLoading.Configuration)
        val configuration = getConfigurationUseCase(Policy.LocalFirst)
        state = state.copy(configuration = configuration)
        stateUpdateFlow.update(PinCodeStateUpdate.Configuration)
        loadingFlow.hide(PinCodeLoading.Configuration)

    }

    /* --- legal checkbox --- */

    private fun setLegalCheckbox(isChecked: Boolean) {
        state = state.copy(legalCheckbox = isChecked)
    }

    /* --- login --- */

    private suspend fun auth(pin: String) {

        loadingFlow.show(PinCodeLoading.Auth)

        val user = pinLoginUseCase(pin)

        loadingFlow.hide(PinCodeLoading.Auth)

        if (user.onBoardingCompleted)
            navigationFlow.navigateTo(PinCodeToMain)
        else
            navigationFlow.navigateTo(PinCodeToOnboarding)

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.UserRegistration,
            EAnalyticsProvider.ALL
        )

    private suspend fun logPrivacyPolicy(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.PrivacyPolicy,
            EAnalyticsProvider.ALL
        )

    private suspend fun logTermsOfService(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.TermsOfService,
            EAnalyticsProvider.ALL
        )

    /* --- state event --- */

    fun execute(stateEvent: PinCodeStateEvent) {
        when (stateEvent) {
            is PinCodeStateEvent.Auth ->
                errorFlow.launchCatch(
                    viewModelScope,
                    PinCodeError.Auth,
                    loadingFlow,
                    PinCodeLoading.Auth
                ) { auth(stateEvent.pin) }
            PinCodeStateEvent.GetConfiguration ->
                errorFlow.launchCatch(
                    viewModelScope,
                    PinCodeError.Configuration,
                    loadingFlow,
                    PinCodeLoading.Configuration
                ) { getConfiguration() }
            PinCodeStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
            PinCodeStateEvent.LogPrivacyPolicy ->
                viewModelScope.launchSafe { logPrivacyPolicy() }
            PinCodeStateEvent.LogTermsOfService ->
                viewModelScope.launchSafe { logTermsOfService() }
            is PinCodeStateEvent.SetLegalCheckbox ->
                viewModelScope.launchSafe { setLegalCheckbox(stateEvent.isChecked) }
        }
    }

}