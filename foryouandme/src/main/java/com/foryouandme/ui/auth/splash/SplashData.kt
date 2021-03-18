package com.foryouandme.ui.auth.splash

import com.foryouandme.core.arch.navigation.NavigationAction

sealed class SplashLoading {

    object Auth : SplashLoading()

}

sealed class SplashStateEvent {

    object Auth : SplashStateEvent()

}

/* --- navigation --- */

object SplashToWelcome : NavigationAction
object SplashToOnboarding : NavigationAction
object SplashToMain : NavigationAction
