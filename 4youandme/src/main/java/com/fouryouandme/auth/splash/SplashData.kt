package com.fouryouandme.auth.splash

import com.fouryouandme.core.arch.navigation.NavigationAction

sealed class SplashLoading {

    object Auth : SplashLoading()

}

/* --- navigation --- */

object SplashToWelcome : NavigationAction
object SplashToScreening : NavigationAction
object SplashToMain : NavigationAction
