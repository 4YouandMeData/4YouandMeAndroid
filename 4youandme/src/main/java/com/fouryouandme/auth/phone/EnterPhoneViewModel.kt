package com.fouryouandme.auth.phone

import androidx.navigation.NavController
import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.deps.modules.AuthModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.navigation.AnywhereToWeb
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.arch.navigation.toastAction
import com.fouryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class EnterPhoneViewModel(
    navigator: Navigator,
    private val authModule: AuthModule
) : BaseViewModel<
        EnterPhoneState,
        EnterPhoneStateUpdate,
        EnterPhoneError,
        EnterPhoneLoading>
    (navigator, EnterPhoneState()) {

    /* --- state update --- */

    suspend fun setCountryNameCode(code: String): Unit =
        setState(
            state().copy(countryNameCode = code)
        ) { EnterPhoneStateUpdate.CountryCode(code) }

    suspend fun setLegalCheckbox(isChecked: Boolean): Unit =
        setState(
            state().copy(legalCheckbox = isChecked)
        ) { EnterPhoneStateUpdate.LegalCheckBox(isChecked) }

    /* --- auth --- */

    suspend fun verifyNumber(
        navController: NavController,
        phoneAndCode: String,
        phone: String,
        countryCode: String
    ): Unit {

        showLoading(EnterPhoneLoading.PhoneNumberVerification)

        authModule.verifyPhoneNumber(phoneAndCode)
            .fold(
                { setError(it, EnterPhoneError.PhoneNumberVerification) },
                { phoneValidationCode(navController, phone, countryCode) }
            )

        hideLoading(EnterPhoneLoading.PhoneNumberVerification)

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