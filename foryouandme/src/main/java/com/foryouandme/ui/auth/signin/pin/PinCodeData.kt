package com.foryouandme.ui.auth.signin.pin

import com.foryouandme.core.arch.navigation.NavigationAction

data class PinCodeState(
    val countryNameCode: String? = null,
    val legalCheckbox: Boolean = false
)

sealed class PinCodeStateUpdate {
    data class CountryCode(val code: String) : PinCodeStateUpdate()
    data class LegalCheckBox(val legalCheckbox: Boolean) : PinCodeStateUpdate()
}

sealed class PinCodeLoading {
    object Auth : PinCodeLoading()
}

sealed class PinCodeError {
    object Auth : PinCodeError()
}

sealed class PinCodeStateEvent {
    data class Auth(val pinCode: String) : PinCodeStateEvent()
}

/* --- navigation --- */

object PinCodeToOnboarding : NavigationAction
object PinCodeToMain : NavigationAction
