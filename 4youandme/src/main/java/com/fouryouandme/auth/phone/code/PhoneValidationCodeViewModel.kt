package com.fouryouandme.auth.phone.code

import com.fouryouandme.auth.AuthNavController
import com.fouryouandme.core.arch.android.BaseViewModel
import com.fouryouandme.core.arch.android.Empty
import com.fouryouandme.core.arch.deps.modules.AuthModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.arch.navigation.Navigator
import com.fouryouandme.core.arch.navigation.RootNavController
import com.fouryouandme.core.arch.navigation.toastAction
import com.fouryouandme.core.cases.auth.AuthUseCase.login
import com.fouryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class PhoneValidationCodeViewModel(
    navigator: Navigator,
    private val authModule: AuthModule
) : BaseViewModel<
        Empty,
        Empty,
        PhoneValidationCodeError,
        PhoneValidationCodeLoading>
    (navigator = navigator, Empty) {


    /* --- auth --- */

    suspend fun auth(
        rootNavController: RootNavController,
        authNavController: AuthNavController,
        phone: String,
        code: String
    ): Unit {

        showLoading(PhoneValidationCodeLoading.Auth)

        val auth = authModule.login(phone, code)

        auth.fold(
            { setError(it, PhoneValidationCodeError.Auth) },
            {
                if (it.onBoardingCompleted) main(rootNavController)
                else screeningQuestions(authNavController)
            }
        )

        hideLoading(PhoneValidationCodeLoading.Auth)

    }

    suspend fun resendCode(
        phoneAndCode: String
    ): Unit {

        showLoading(PhoneValidationCodeLoading.ResendCode)

        authModule.verifyPhoneNumber(phoneAndCode)
            .fold(
                { setError(it, PhoneValidationCodeError.ResendCode) },
                // TODO: fix hardcoded text
                { navigator.performAction(toastAction("Code sent successfully")) }
            )

        hideLoading(PhoneValidationCodeLoading.ResendCode)

    }

    /* --- navigation --- */

    suspend fun back(
        authNavController: AuthNavController,
        rootNavController: RootNavController
    ): Unit {
        if (navigator.back(authNavController).not())
            navigator.back(rootNavController)
    }

    private suspend fun screeningQuestions(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, PhoneValidationCodeToScreening)

    private suspend fun main(rootNavController: RootNavController): Unit =
        navigator.navigateTo(rootNavController, PhoneValidationCodeToMain)

    suspend fun toastError(error: FourYouAndMeError): Unit =
        navigator.performAction(toastAction(error))

}