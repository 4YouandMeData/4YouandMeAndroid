package org.fouryouandme.auth.signup.info

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class SignUpInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        SignUpInfoState,
        SignUpInfoStateUpdate,
        SignUpInfoError,
        SignUpInfoError>
    (SignUpInfoState(), navigator, runtime) {

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, SignUpInfoError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        SignUpInfoStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()
}