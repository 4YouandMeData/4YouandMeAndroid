package com.foryouandme.auth.splash

import arrow.core.getOrElse
import arrow.syntax.function.pipe
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getUser
import com.foryouandme.core.cases.auth.AuthUseCase.isLogged
import com.foryouandme.core.cases.auth.AuthUseCase.updateUserTimeZone
import com.foryouandme.core.cases.push.PushUseCase
import com.foryouandme.core.ext.startCoroutineAsync
import org.threeten.bp.ZoneId
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

            startCoroutineAsync { authModule.updateUserTimeZone(ZoneId.systemDefault()) }

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
        navigator.navigateTo(authNavController, SplashToVideo)

}