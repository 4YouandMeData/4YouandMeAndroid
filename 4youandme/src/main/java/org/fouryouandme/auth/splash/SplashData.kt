package org.fouryouandme.auth.splash

import org.fouryouandme.core.arch.navigation.NavigationAction

sealed class SplashLoading {

    object Configuration : SplashLoading()

}

sealed class SplashError {

    object Configuration : SplashError()

}


/* --- navigation --- */

object SplashToWelcome : NavigationAction
