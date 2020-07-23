package org.fouryouandme.main

import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class MainViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        MainState,
        MainStateUpdate,
        MainError,
        MainLoading>
    (MainState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(rootNavController: RootNavController): Unit =
        runtime.fx.concurrent {

            !showLoading(MainLoading.Initialization)

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)
                    .handleAuthError(runtime, rootNavController, navigator)

            !configuration.fold(
                { setError(it, MainError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        MainStateUpdate.Initialization(it)
                    )
                }
            )

            !hideLoading(MainLoading.Initialization)

        }.unsafeRunAsync()

    /* --- page --- */

    fun setPage(id: Int): Unit =
        setState(
            state().copy(selectedPage = id),
            MainStateUpdate.Page(id)
        ).unsafeRunAsync()

}