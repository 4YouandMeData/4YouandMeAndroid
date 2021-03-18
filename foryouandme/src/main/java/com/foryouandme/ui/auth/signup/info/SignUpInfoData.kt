package com.foryouandme.ui.auth.signup.info

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class SignUpInfoState(val configuration: Configuration? = null)

sealed class SignUpInfoStateUpdate {

    data class Config(val configuration: Configuration): SignUpInfoStateUpdate()

}

sealed class SignUpInfoLoading {

    object Configuration : SignUpInfoLoading()

}

sealed class SignUpInfoError {

    object Configuration : SignUpInfoError()

}

sealed class SignUpInfoStateEvent {

    object GetConfiguration : SignUpInfoStateEvent()
    object ScreenViewed : SignUpInfoStateEvent()

}

/* --- navigation --- */

object SignUpInfoToSignUpLater : NavigationAction

object SignUpInfoToEnterPhone : NavigationAction