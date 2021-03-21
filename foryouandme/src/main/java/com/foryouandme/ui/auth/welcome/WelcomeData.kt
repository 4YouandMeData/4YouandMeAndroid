package com.foryouandme.ui.auth.welcome

import com.foryouandme.core.arch.navigation.NavigationAction

sealed class WelcomeStateEvent {
    object ScreenViewed : WelcomeStateEvent()
}

/* --- navigation --- */

object WelcomeToSignUpInfo : NavigationAction