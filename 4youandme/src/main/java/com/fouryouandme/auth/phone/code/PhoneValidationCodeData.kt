package com.fouryouandme.auth.phone.code

import com.fouryouandme.core.arch.navigation.NavigationAction

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

object PhoneValidationCodeToScreening : NavigationAction
object PhoneValidationCodeToMain : NavigationAction
