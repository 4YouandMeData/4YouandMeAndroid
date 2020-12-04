package com.foryouandme.ui.auth.phone.code

import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.android.Empty
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.toastAction
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthUseCase.login
import com.foryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class PhoneValidationCodeViewModel(
    navigator: Navigator,
    private val authModule: AuthModule,
    private val analyticsModule: AnalyticsModule
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
        code: String,
        countryCode: String
    ): Unit {

        showLoading(PhoneValidationCodeLoading.Auth)

        val auth =
            authModule.login(phone, code, countryCode)

        auth.fold(
            { setError(it, PhoneValidationCodeError.Auth) },
            {
                if (it.onBoardingCompleted) main(rootNavController)
                else onboarding(authNavController)
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

    private suspend fun onboarding(authNavController: AuthNavController): Unit =
        navigator.navigateTo(authNavController, PhoneValidationCodeToOnboarding)

    private suspend fun main(rootNavController: RootNavController): Unit =
        navigator.navigateTo(rootNavController, PhoneValidationCodeToMain)

    suspend fun toastError(error: ForYouAndMeError): Unit =
        navigator.performAction(toastAction(error))


    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(AnalyticsEvent.ScreenViewed.OtpValidation, EAnalyticsProvider.ALL)

}