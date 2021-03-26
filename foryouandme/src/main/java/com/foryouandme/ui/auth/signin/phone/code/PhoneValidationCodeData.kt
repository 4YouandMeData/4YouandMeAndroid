package com.foryouandme.ui.auth.signin.phone.code

import com.foryouandme.core.arch.navigation.NavigationAction

sealed class PhoneValidationCodeLoading {
    object Auth : PhoneValidationCodeLoading()
    object ResendCode : PhoneValidationCodeLoading()
}

sealed class PhoneValidationCodeError {
    object Auth : PhoneValidationCodeError()
    object ResendCode : PhoneValidationCodeError()
}

sealed class PhoneValidationStateEvent {

    data class Auth(
        val phone: String,
        val code: String,
        val countryCode: String
    ) : PhoneValidationStateEvent()

    data class ResendCode(val phoneAndCode: String) : PhoneValidationStateEvent()

    object LogScreenViewed : PhoneValidationStateEvent()

}

/* --- navigation --- */

object PhoneValidationCodeToOnboarding : NavigationAction
object PhoneValidationCodeToMain : NavigationAction
