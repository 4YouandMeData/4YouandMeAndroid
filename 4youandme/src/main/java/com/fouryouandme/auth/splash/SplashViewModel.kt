package com.fouryouandme.auth.splash

import arrow.core.getOrElse
import arrow.syntax.function.pipe
import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.deps.modules.AuthModule
import com.fouryouandme.core.arch.deps.modules.nullToError
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getUser
import com.fouryouandme.core.cases.auth.AuthUseCase.isLogged
import com.fouryouandme.core.cases.push.PushUseCase
import timber.log.Timber

class SplashViewModel(
    navigator: Navigator,
    private val authModule: AuthModule
) : BaseViewModel<Empty, Empty, Empty, SplashLoading>
    (navigator, Empty) {

    suspend fun auth(
        rootNavController: RootNavController,
        authNavController: AuthNavController
    ): Unit {

        showLoading(SplashLoading.Auth)

        // Token logging for debug
        PushUseCase.getPushToken()
            .getOrElse { "Error: can't load the token" }
            .pipe { Timber.tag("FCM_TOKEN").d(it) }

        if (authModule.isLogged()) {

            val user =
                authModule.getUser(CachePolicy.Network)
                    .nullToError()

            user.fold(
                { welcome(authNavController) },
                {
                    if (it.onBoardingCompleted) main(rootNavController)
                    else screening(authNavController)
                }
            )

        } else welcome(authNavController)

        hideLoading(SplashLoading.Auth)

    }

    private suspend fun main(rootNavController: RootNavController): Unit =
        navigator.navigateTo(rootNavController, SplashToMain)

    private suspend fun welcome(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SplashToWelcome)

    private suspend fun screening(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, SplashToScreening)

}