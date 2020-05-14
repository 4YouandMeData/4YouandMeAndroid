package org.fouryouandme.auth.phone.code

import androidx.navigation.NavController
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class PhoneValidationCodeViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        PhoneValidationCodeState,
        PhoneValidationCodeStateUpdate,
        PhoneValidationCodeError,
        PhoneValidationCodeError>
    (PhoneValidationCodeState(), navigator, runtime) {

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, PhoneValidationCodeError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        PhoneValidationCodeStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

}