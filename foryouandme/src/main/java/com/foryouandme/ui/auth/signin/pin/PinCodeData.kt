package com.foryouandme.ui.auth.signin.pin

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class PinCodeState(
    val configuration: Configuration? = null,
    val countryNameCode: String? = null,
    val legalCheckbox: Boolean = false
)

sealed class PinCodeStateUpdate {
    object Configuration : PinCodeStateUpdate()
    data class CountryCode(val code: String) : PinCodeStateUpdate()
    data class LegalCheckBox(val legalCheckbox: Boolean) : PinCodeStateUpdate()
}

sealed class PinCodeLoading {
    object Configuration : PinCodeLoading()
    object Auth : PinCodeLoading()
}

sealed class PinCodeError {
    object Configuration : PinCodeError()
    object Auth : PinCodeError()
}

sealed class PinCodeStateEvent {
    object GetConfiguration : PinCodeStateEvent()
    data class Auth(val pin: String) : PinCodeStateEvent()
    object ScreenViewed: PinCodeStateEvent()
    data class SetLegalCheckbox(val isChecked: Boolean): PinCodeStateEvent()
    object LogPrivacyPolicy: PinCodeStateEvent()
    object LogTermsOfService: PinCodeStateEvent()

}

/* --- navigation --- */

object PinCodeToOnboarding : NavigationAction
object PinCodeToMain : NavigationAction
