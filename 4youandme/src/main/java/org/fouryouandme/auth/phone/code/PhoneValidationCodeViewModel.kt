package org.fouryouandme.auth.phone.code

import androidx.navigation.NavController
import arrow.Kind
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.R
import org.fouryouandme.auth.phone.EnterPhoneError
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
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
        PhoneValidationCodeLoading>
    (PhoneValidationCodeState(), navigator, runtime) {

    /* --- data --- */

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

    /* --- auth --- */

    fun auth(navController: NavController, phone: String, code: String): Unit =
        runtime.fx.concurrent {

            !showLoading(PhoneValidationCodeLoading.Auth)

            val auth =
                !AuthUseCase.login(
                    runtime,
                    phone,
                    code,
                    state().configuration
                        .map { it.text.phoneVerification.error.errorWrongCode }
                        .getOrElse { getString(R.string.ERROR_generic) }
                )

            !auth.fold(
                { setError(it, PhoneValidationCodeError.Auth) },
                { numberVerificationSuccess() }
            )

            !hideLoading(PhoneValidationCodeLoading.Auth)

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    private fun numberVerificationSuccess(): Kind<ForIO, Unit> =
        navigator.performAction(runtime, toastAction("Phone number verification SUCCESS"))

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()

}