package org.fouryouandme.auth.welcome

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class WelcomeViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        WelcomeState,
        WelcomeStateUpdate,
        WelcomeError,
        WelcomeLoading>
    (WelcomeState(), navigator, runtime) {

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, WelcomeError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        WelcomeStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()

    fun signUpInfo(navController: NavController): Unit =
        navigator.navigateTo(runtime, navController, WelcomeToSignUpInfo).unsafeRunAsync()
}