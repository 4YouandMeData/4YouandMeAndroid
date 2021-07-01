package com.foryouandme.ui.auth.signin.pin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.foryouandme.domain.usecase.auth.PinLoginUseCase
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.ui.compose.error.toForYouAndMeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PinCodeViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val pinLoginUseCase: PinLoginUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(PinCodeState())
    val stateFlow = state as StateFlow<PinCodeState>

    /* --- event --- */

    private val events = MutableSharedFlow<UIEvent<PinCodeEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<PinCodeEvent>>

    /* --- init --- */

    init {
        execute(PinCodeAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- legal checkbox --- */

    private suspend fun setLegalCheckbox(isChecked: Boolean) {
        state.emit(state.value.copy(legalCheckbox = isChecked))
    }

    /* --- login --- */

    private fun auth(): Action =
        action(
            {
                state.emit(state.value.copy(auth = LazyData.Loading()))
                val user = pinLoginUseCase(state.value.pin)
                state.emit(state.value.copy(auth = LazyData.unit()))
                if (user.onBoardingCompleted)
                    events.emit(PinCodeEvent.Main.toUIEvent())
                else
                    events.emit(PinCodeEvent.Onboarding.toUIEvent())

            },
            {
                val error = it.toForYouAndMeException()
                state.emit(state.value.copy(auth = LazyData.Error(error)))
                events.emit(PinCodeEvent.AuthError(error).toUIEvent())
            }
        )

    /* --- pin --- */

    private suspend fun setPin(pin: String) {
        state.emit(state.value.copy(pin = pin, isPinValid = isPinValid(pin)))
    }

    private fun isPinValid(pin: String): Boolean = pin.isBlank().not() && pin.isEmpty().not()

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.UserRegistration,
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logPrivacyPolicy() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.PrivacyPolicy,
            EAnalyticsProvider.ALL
        )
    }

    private suspend fun logTermsOfService() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.TermsOfService,
            EAnalyticsProvider.ALL
        )
    }

    /* --- action --- */

    fun execute(action: PinCodeAction) {
        when (action) {
            PinCodeAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            is PinCodeAction.Auth ->
                viewModelScope.launchAction(auth())
            is PinCodeAction.SetPin ->
                viewModelScope.launchSafe { setPin(action.pin) }
            PinCodeAction.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
            PinCodeAction.LogPrivacyPolicy ->
                viewModelScope.launchSafe { logPrivacyPolicy() }
            PinCodeAction.LogTermsOfService ->
                viewModelScope.launchSafe { logTermsOfService() }
            is PinCodeAction.SetLegalCheckbox ->
                viewModelScope.launchSafe { setLegalCheckbox(action.isChecked) }
        }
    }

}