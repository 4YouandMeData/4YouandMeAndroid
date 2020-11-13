package com.foryouandme.auth.phone.code

import com.foryouandme.core.arch.navigation.NavigationAction

sealed class PhoneValidationCodeLoading {
    object Auth : PhoneValidationCodeLoading()
    object ResendCode : PhoneValidationCodeLoading()
}

sealed class PhoneValidationCodeError {
    object Initialization : PhoneValidationCodeError()
    object Auth : PhoneValidationCodeError()
    object ResendCode : PhoneValidationCodeError()
}

/* --- navigation --- */

object PhoneValidationCodeToVideo : NavigationAction
object PhoneValidationCodeToMain : NavigationAction
