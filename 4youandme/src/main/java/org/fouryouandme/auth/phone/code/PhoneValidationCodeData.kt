package org.fouryouandme.auth.phone.code

import org.fouryouandme.core.arch.navigation.NavigationAction

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
