package org.fouryouandme.auth.splash

import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getUser
import org.fouryouandme.core.cases.auth.AuthUseCase.isLogged

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