package org.fouryouandme.auth.phone.code

import arrow.fx.ForIO
import org.fouryouandme.auth.AuthNavController
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.arch.navigation.toastAction
import org.fouryouandme.core.cases.auth.AuthUseCase.login
import org.fouryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class PhoneValidationCodeViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>,
    private val authModule: AuthModule
) : BaseViewModel<
        ForIO,
        Empty,
        Empty,
        PhoneValidationCodeError,
        PhoneValidationCodeLoading>
    (Empty, navigator = navigator, runtime = runtime) {


    /* --- auth --- */

    suspend fun auth(
        rootNavController: RootNavController,
        authNavController: AuthNavController,
        phone: String,
        code: String
    ): Unit {

        showLoadingFx(PhoneValidationCodeLoading.Auth)

        val auth = authModule.login(phone, code)

        auth.fold(
            { setErrorFx(it, PhoneValidationCodeError.Auth) },
            {
                //if (it.onBoardingCompleted) main(rootNavController)
                //else screeningQuestions(authNavController)
                screeningQuestions(authNavController)
            }
        )

        hideLoadingFx(PhoneValidationCodeLoading.Auth)

    }

    suspend fun resendCode(
        phoneAndCode: String
    ): Unit {

        showLoadingFx(PhoneValidationCodeLoading.ResendCode)

        authModule.verifyPhoneNumber(phoneAndCode)
            .fold(
                { setErrorFx(it, PhoneValidationCodeError.ResendCode) },
                // TODO: fix hardcoded text
                { navigator.performAction(toastAction("Code sent successfully")) }
            )

        hideLoadingFx(PhoneValidationCodeLoading.ResendCode)

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