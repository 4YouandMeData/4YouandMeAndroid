package com.foryouandme.ui.auth.signin.phone.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.action.toastAction
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.PhoneLoginUseCase
import com.foryouandme.domain.usecase.auth.VerifyPhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhoneValidationCodeViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<PhoneValidationCodeLoading>,
    private val errorFlow: ErrorFlow<PhoneValidationCodeError>,
    private val navigationFlow: NavigationFlow,
    private val navigator: Navigator,
    private val phoneLoginUseCase: PhoneLoginUseCase,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    /* --- flow --- */

    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- auth --- */

    private suspend fun auth(
        phone: String,
        code: String,
        countryCode: String
    ) {

        loadingFlow.show(PhoneValidationCodeLoading.Auth)
        val user = phoneLoginUseCase(phone, code, countryCode)
        loadingFlow.hide(PhoneValidationCodeLoading.Auth)
        if (user.onBoardingCompleted) navigationFlow.navigateTo(PhoneValidationCodeToMain)
        else navigationFlow.navigateTo(PhoneValidationCodeToOnboarding)

    }

    private suspend fun resendCode(phoneAndCode: String) {

        loadingFlow.show(PhoneValidationCodeLoading.ResendCode)
        verifyPhoneNumberUseCase(phoneAndCode)
        loadingFlow.hide(PhoneValidationCodeLoading.ResendCode)

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed() {
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.OtpValidation,
            EAnalyticsProvider.ALL
        )
    }

    /* --- state event --- */

    fun execute(stateEvent: PhoneValidationStateEvent) {
        when (stateEvent) {
            is PhoneValidationStateEvent.Auth ->
                errorFlow.launchCatch(
                    viewModelScope,
                    PhoneValidationCodeError.Auth,
                    loadingFlow,
                    PhoneValidationCodeLoading.Auth
                ) { auth(stateEvent.phone, stateEvent.code, stateEvent.countryCode) }
            is PhoneValidationStateEvent.ResendCode ->
                errorFlow.launchCatch(
                    viewModelScope,
                    PhoneValidationCodeError.ResendCode,
                    loadingFlow,
                    PhoneValidationCodeLoading.ResendCode
                ) { resendCode(stateEvent.phoneAndCode) }
            PhoneValidationStateEvent.LogScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
        }
    }

}