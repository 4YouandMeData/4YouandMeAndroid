package org.fouryouandme.auth.phone

import androidx.navigation.NavController
import arrow.fx.ForIO
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.navigation.AnywhereToWeb
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class EnterPhoneViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val authModule: AuthModule
) : BaseViewModel<
        ForIO,
        EnterPhoneState,
        EnterPhoneStateUpdate,
        EnterPhoneError,
        EnterPhoneLoading>
    (EnterPhoneState(), navigator, runtime) {

    /* --- state update --- */

    suspend fun setCountryNameCode(code: String): Unit =
        setStateFx(
            state().copy(countryNameCode = code)
        ) { EnterPhoneStateUpdate.CountryCode(code) }

    suspend fun setLegalCheckbox(isChecked: Boolean): Unit =
        setStateFx(
            state().copy(legalCheckbox = isChecked)
        ) { EnterPhoneStateUpdate.LegalCheckBox(isChecked) }

    /* --- auth --- */

    suspend fun verifyNumber(
        navController: NavController,
        phoneAndCode: String,
        phone: String,
        countryCode: String
    ): Unit {

        showLoadingFx(EnterPhoneLoading.PhoneNumberVerification)

        authModule.verifyPhoneNumber(phoneAndCode)
            .fold(
                { setErrorFx(it, EnterPhoneError.PhoneNumberVerification) },
                { phoneValidationCode(navController, phone, countryCode) }
            )

        hideLoadingFx(EnterPhoneLoading.PhoneNumberVerification)

    }

    /* --- navigation --- */

    suspend fun back(
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(authNavController).not())
            navigator.back(rootNavController)
    }

    private suspend fun phoneValidationCode(
        navController: NavController,
        phone: String,
        countryCode: String
    ): Unit =
        navigator.navigateTo(
            navController,
            EnterPhoneToPhoneValidationCode(phone, countryCode)
        )

    suspend fun web(rootNavController: RootNavController, url: String): Unit =
        navigator.navigateTo(rootNavController, AnywhereToWeb(url))

    suspend fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(toastAction(error))
}