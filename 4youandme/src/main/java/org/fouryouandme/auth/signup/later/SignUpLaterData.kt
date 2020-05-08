package org.fouryouandme.auth.signup.later

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.entity.configuration.Configuration

data class SignUpLaterState(val configuration: Option<Configuration> = None)

sealed class SignUpLaterStateUpdate {
    data class Initialization(val configuration: Configuration): SignUpLaterStateUpdate()
}

sealed class SignUpLaterLoading {
    object Initialization: SignUpLaterLoading()
}

sealed class SignUpLaterError {
    object Initialization: SignUpLaterError()
}