package com.foryouandme.aboutyou

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getUser

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
    ): Either<ForYouAndMeError, AboutYouState> {

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
    ): Either<ForYouAndMeError, AboutYouState> {

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