package com.foryouandme.ui.auth.phone

import com.foryouandme.ui.auth.AuthNavController
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.modules.AnalyticsModule
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.navigation.AnywhereToWeb
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.arch.navigation.toastAction
import com.foryouandme.core.cases.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthUseCase.verifyPhoneNumber

class EnterPhoneViewModel(
    navigator: Navigator,
    private val authModule: AuthModule,
    private val analyticsModule: AnalyticsModule
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

    /* --- analytics --- */

    suspend fun logScreenViewed(): Unit =
        analyticsModule.logEvent(
            AnalyticsEvent.ScreenViewed.UserRegistration,
            EAnalyticsProvider.ALL
        )

    suspend fun logPrivacyPolicy(): Unit =
        analyticsModule.logEvent(
            AnalyticsEvent.ScreenViewed.PrivacyPolicy,
            EAnalyticsProvider.ALL
        )

    suspend fun logTermsOfService(): Unit =
        analyticsModule.logEvent(
            AnalyticsEvent.ScreenViewed.TermsOfService,
            EAnalyticsProvider.ALL
        )

}