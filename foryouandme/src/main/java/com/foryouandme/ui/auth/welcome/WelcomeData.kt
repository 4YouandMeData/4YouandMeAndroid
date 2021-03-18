package com.foryouandme.ui.auth.welcome

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration

data class WelcomeState(val configuration: Configuration? = null)

sealed class WelcomeStateUpdate {

    data class Config(val configuration: Configuration): WelcomeStateUpdate()

}

sealed class WelcomeLoading {

    object Configuration : WelcomeLoading()

}

sealed class WelcomeError {

    object Configuration : WelcomeError()

}

sealed class WelcomeStateEvent {

    object GetConfiguration : WelcomeStateEvent()
    object ScreenViewed : WelcomeStateEvent()

}

/* --- navigation --- */

object WelcomeToSignUpInfo : NavigationAction