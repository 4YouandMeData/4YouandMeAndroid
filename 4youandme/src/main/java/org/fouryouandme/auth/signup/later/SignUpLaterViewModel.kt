package org.fouryouandme.auth.signup.later

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.auth.signup.info.SignUpInfoToSignUpLater
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class SignUpLaterViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        SignUpLaterState,
        SignUpLaterStateUpdate,
        SignUpLaterError,
        SignUpLaterError>
    (SignUpLaterState(), navigator, runtime) {

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, SignUpLaterError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        SignUpLaterStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()
}