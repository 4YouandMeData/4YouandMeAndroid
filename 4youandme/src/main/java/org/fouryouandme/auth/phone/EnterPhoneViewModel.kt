package org.fouryouandme.auth.phone

import androidx.navigation.NavController
import arrow.Kind
import arrow.core.getOrElse
import arrow.core.some
import arrow.core.toOption
import arrow.fx.ForIO
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import org.fouryouandme.core.ext.unsafeRunAsync

class EnterPhoneViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        EnterPhoneState,
        EnterPhoneStateUpdate,
        EnterPhoneError,
        EnterPhoneLoading>
    (EnterPhoneState(), navigator, runtime) {

    /* --- data --- */

    fun initialize(): Unit =
        runtime.fx.concurrent {

            val configuration =
                !ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryFirst)

            !configuration.fold(
                { setError(it, EnterPhoneError.Initialization) },
                {
                    setState(
                        state().copy(configuration = it.toOption()),
                        EnterPhoneStateUpdate.Initialization(it)
                    )
                }
            )

        }.unsafeRunAsync()

    fun setCountryNameCode(code: String): Unit =
        setState(
            state().copy(countryNameCode = code.some()),
            EnterPhoneStateUpdate.CountryCode(code)
        ).unsafeRunAsync()

    /* --- auth --- */

    fun verifyNumber(
        navController: NavController,
        phoneAndCode: String,
        phone: String,
        countryCode: String
    ): Unit =
        runtime.fx.concurrent {

            !showLoading(EnterPhoneLoading.PhoneNumberVerification)

            val auth =
                !AuthUseCase.verifyPhoneNumber(
                    runtime,
                    phoneAndCode
                )

            !auth.fold(
                { setError(it, EnterPhoneError.PhoneNumberVerification) },
                { phoneValidationCode(navController, phone, countryCode) }
            )

            !hideLoading(EnterPhoneLoading.PhoneNumberVerification)

        }.unsafeRunAsync()

    /* --- navigation --- */

    fun back(navController: NavController): Unit =
        navigator.back(runtime, navController).unsafeRunAsync()

    private fun phoneValidationCode(
        navController: NavController,
        phone: String,
        countryCode: String
    ): Kind<ForIO, Unit> =
        navigator.navigateTo(
            runtime,
            navController,
            EnterPhoneToPhoneValidationCode(phone, countryCode)
        )

    fun web(navController: NavController, url: String): Unit =
        navigator.navigateTo(runtime, navController, EnterPhoneToWeb(url)).unsafeRunAsync()

    fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(runtime, toastAction(error)).unsafeRunAsync()
}