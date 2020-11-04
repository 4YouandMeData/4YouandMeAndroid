package com.foryouandme.auth.phone

import androidx.navigation.NavController
import com.foryouandme.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.toastAction
import com.foryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

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
        authNavController: AuthNavController,
        phoneAndCode: String,
        phone: String,
        countryCode: String
    ): Unit {

        showLoading(EnterPhoneLoading.PhoneNumberVerification)

        authModule.verifyPhoneNumber(phoneAndCode)
            .fold(
                { setError(it, EnterPhoneError.PhoneNumberVerification) },
                { phoneValidationCode(authNavController, phone, countryCode) }
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
        authNavController: AuthNavController,
        phone: String,
        countryCode: String
    ): Unit =
        navigator.navigateTo(
            authNavController,
            EnterPhoneToPhoneValidationCode(phone, countryCode)
        )

    suspend fun web(rootNavController: RootNavController, url: String): Unit =
        navigator.navigateTo(rootNavController, AnywhereToWeb(url))

    suspend fun toastError(error: ForYouAndMeError): Unit =
        navigator.performAction(toastAction(error))
}