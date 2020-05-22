package org.fouryouandme.auth.phone.code

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration

data class PhoneValidationCodeState(val configuration: Option<Configuration> = None)

sealed class PhoneValidationCodeStateUpdate {
    data class Initialization(val configuration: Configuration) : PhoneValidationCodeStateUpdate()
}

sealed class PhoneValidationCodeLoading {
    object Initialization : PhoneValidationCodeLoading()
    object Auth : PhoneValidationCodeLoading()
    object ResendCode : PhoneValidationCodeLoading()
}

sealed class PhoneValidationCodeError {
    object Initialization : PhoneValidationCodeError()
    object Auth : PhoneValidationCodeError()
    object ResendCode : PhoneValidationCodeError()
}

/* --- navigation --- */

object PhoneValidationCodeToScreeningQuestions: NavigationAction
