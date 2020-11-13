package com.foryouandme.auth.splash

import com.foryouandme.core.arch.navigation.NavigationAction

sealed class SplashLoading {

    object Auth : SplashLoading()

}

/* --- navigation --- */

object SplashToWelcome : NavigationAction
object SplashToVideo : NavigationAction
object SplashToMain : NavigationAction
