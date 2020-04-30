package org.fouryouandme.auth.splash

sealed class SplashLoading {

    object Configuration : SplashLoading()

}

sealed class SplashError {

    object Configuration : SplashError()

}

