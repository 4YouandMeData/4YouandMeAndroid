package com.foryouandme.ui.auth.signin.pin

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class PinCodeState(
    val legalCheckbox: Boolean = false
)

sealed class PinCodeLoading {
    object Auth : PinCodeLoading()
}

sealed class PinCodeError {
    object Auth : PinCodeError()
}

sealed class PinCodeStateEvent {
    data class Auth(val pin: String) : PinCodeStateEvent()
    data class SetLegalCheckbox(val isChecked: Boolean) : PinCodeStateEvent()
    object ScreenViewed : PinCodeStateEvent()
    object LogPrivacyPolicy : PinCodeStateEvent()
    object LogTermsOfService : PinCodeStateEvent()
}

/* --- navigation --- */

object PinCodeToOnboarding : NavigationAction
object PinCodeToMain : NavigationAction
