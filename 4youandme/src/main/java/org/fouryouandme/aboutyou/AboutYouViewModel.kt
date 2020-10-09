package org.fouryouandme.aboutyou

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
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
    runtime: Runtime<ForIO>,
    private val authModule: AuthModule
) : BaseViewModel<
        ForIO,
        AboutYouState,
        AboutYouStateUpdate,
        AboutYouError,
        AboutYouLoading>
    (
    navigator = navigator,
    runtime = runtime
) {

    /* --- initialization --- */

    suspend fun initialize(
        rootNavController: RootNavController,
        refreshFromNetwork: Boolean
    ): Either<FourYouAndMeError, AboutYouState> {

        showLoadingFx(AboutYouLoading.Initialization)

        val state =
            authModule.getUser(
                if (refreshFromNetwork) CachePolicy.Network
                else CachePolicy.MemoryFirst
            )
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setErrorFx(it, AboutYouError.Initialization)
                        it.left()
                    },
                    { user ->

                        val state = AboutYouState(user)

                        setStateFx(state) { AboutYouStateUpdate.Initialization(it.user) }

                        state.right()
                    }
                )

        hideLoadingFx(AboutYouLoading.Initialization)

        return state

    }

    suspend fun refreshUser(
        rootNavController: RootNavController,
        refreshFromNetwork: Boolean
    ): Either<FourYouAndMeError, AboutYouState> {

        showLoadingFx(AboutYouLoading.Refresh)

        val state =
            authModule.getUser(
                if (refreshFromNetwork) CachePolicy.Network
                else CachePolicy.MemoryFirst
            )
                .nullToError()
                .handleAuthError(rootNavController, navigator)
                .fold(
                    {
                        setErrorFx(it, AboutYouError.Refresh)
                        it.left()
                    },
                    { user ->

                        val state = AboutYouState(user)

                        setStateFx(state) { AboutYouStateUpdate.Refresh(it.user) }

                        state.right()
                    }
                )

        hideLoadingFx(AboutYouLoading.Refresh)

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