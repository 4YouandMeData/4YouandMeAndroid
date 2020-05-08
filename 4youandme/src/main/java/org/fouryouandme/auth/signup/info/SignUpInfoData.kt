package org.fouryouandme.auth.signup.info

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration

data class SignUpInfoState(val configuration: Option<Configuration> = None)

sealed class SignUpInfoStateUpdate {
    data class Initialization(val configuration: Configuration): SignUpInfoStateUpdate()
}

sealed class SignUpInfoLoading {
    object Initialization: SignUpInfoLoading()
}

sealed class SignUpInfoError {
    object Initialization: SignUpInfoError()
}

/* --- navigation --- */

object SignUpInfoToSignUpLater: NavigationAction

object SignUpInfoToEnterPhone: NavigationAction