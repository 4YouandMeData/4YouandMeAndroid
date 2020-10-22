package org.fouryouandme.aboutyou

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getUser

class AboutYouViewModel(
    navigator: Navigator,
    private val authModule: AuthModule
) : BaseViewModel<
        AboutYouState,
        AboutYouStateUpdate,
        AboutYouError,
        AboutYouLoading>(navigator) {

    /* --- initialization --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        refreshFromNetwork: Boolean
    ): Either<FourYouAndMeError, AboutYouState> {

        showLoading(AboutYouLoading.Initialization)

        val state =
            authModule.getUser(
                if (refreshFromNetwork) CachePolicy.Network
                else CachePolicy.MemoryFirst
            )
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, AboutYouError.Initialization)
                        it.left()
                    },
                    { user ->

                        val state = AboutYouState(user)

                        setState(state) { AboutYouStateUpdate.Initialization(it.user) }

                        state.right()
                    }
                )

        hideLoading(AboutYouLoading.Initialization)

        return state

    }

    suspend fun refreshUser(
        rootNavController: RootNavController,
        refreshFromNetwork: Boolean
    ): Either<FourYouAndMeError, AboutYouState> {

        showLoading(AboutYouLoading.Refresh)

        val state =
            authModule.getUser(
                if (refreshFromNetwork) CachePolicy.Network
                else CachePolicy.MemoryFirst
            )
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setError(it, AboutYouError.Refresh)
                        it.left()
                    },
                    { user ->

                        val state = AboutYouState(user)

                        setState(state) { AboutYouStateUpdate.Refresh(it.user) }

                        state.right()
                    }
                )

        hideLoading(AboutYouLoading.Refresh)

        return state
    }

    /* --- navigation --- */

    suspend fun back(
        aboutYouNavController: AboutYouNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(aboutYouNavController).not())
            navigator.back(rootNavController)
    }
}