package org.fouryouandme.main

import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.R
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

    fun setRestorePage(id: Int): Unit =
        setState(
            state().copy(restorePage = id),
            MainStateUpdate.RestorePage(id)
        ).unsafeRunAsync()

    fun selectFeed(): Unit =
        setState(
            state(),
            MainStateUpdate.PageNavigation(R.id.feed_navigation)
        ).unsafeRunAsync()

    fun getPagedIds(): List<Int> =
        listOf(
            R.navigation.feed_navigation,
            R.navigation.tasks_navigation,
            R.navigation.user_data_navigation,
            R.navigation.study_info_navigation
        )

}