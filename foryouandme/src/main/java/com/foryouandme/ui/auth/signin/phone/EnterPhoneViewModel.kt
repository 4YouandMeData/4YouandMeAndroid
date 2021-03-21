package com.foryouandme.ui.auth.signin.phone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.ErrorFlow
import com.foryouandme.core.arch.flow.LoadingFlow
import com.foryouandme.core.arch.flow.NavigationFlow
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.auth.VerifyPhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnterPhoneViewModel @Inject constructor(
    private val loadingFlow: LoadingFlow<EnterPhoneLoading>,
    private val errorFlow: ErrorFlow<EnterPhoneError>,
    private val navigationFlow: NavigationFlow,
    private val verifyPhoneNumberUseCase: VerifyPhoneNumberUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase
) : ViewModel() {

    var state = EnterPhoneState()
        private set

    /* --- flow --- */

    val loading = loadingFlow.loading
    val error = errorFlow.error
    val navigation = navigationFlow.navigation

    /* --- country code --- */

    private fun setCountryNameCode(code: String) {
        state = state.copy(countryNameCode = code)
    }

    /* --- legal checkbox --- */

    private fun setLegalCheckbox(isChecked: Boolean) {
        state = state.copy(legalCheckbox = isChecked)
    }

    /* --- auth --- */

    private suspend fun verifyNumber(
        phoneAndCode: String,
        phone: String,
        countryCode: String
    ) {

        loadingFlow.show(EnterPhoneLoading.PhoneNumberVerification)
        verifyPhoneNumberUseCase(phoneAndCode)
        loadingFlow.hide(EnterPhoneLoading.PhoneNumberVerification)
        navigationFlow.navigateTo(EnterPhoneToPhoneValidationCode(phone, countryCode))

    }

    /* --- analytics --- */

    private suspend fun logScreenViewed(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.UserRegistration,
            EAnalyticsProvider.ALL
        )

    private suspend fun logPrivacyPolicy(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.PrivacyPolicy,
            EAnalyticsProvider.ALL
        )

    private suspend fun logTermsOfService(): Unit =
        sendAnalyticsEventUseCase(
            AnalyticsEvent.ScreenViewed.TermsOfService,
            EAnalyticsProvider.ALL
        )

    /* --- state event --- */

    fun execute(stateEvent: EnterPhoneStateEvent) {
        when (stateEvent) {
            is EnterPhoneStateEvent.VerifyPhoneNumber ->
                errorFlow.launchCatch(
                    viewModelScope,
                    EnterPhoneError.PhoneNumberVerification,
                    loadingFlow,
                    EnterPhoneLoading.PhoneNumberVerification
                ) {
                    verifyNumber(
                        stateEvent.phoneAndCode,
                        stateEvent.phone,
                        stateEvent.countryCode
                    )
                }
            EnterPhoneStateEvent.ScreenViewed ->
                viewModelScope.launchSafe { logScreenViewed() }
            EnterPhoneStateEvent.LogPrivacyPolicy ->
                viewModelScope.launchSafe { logPrivacyPolicy() }
            EnterPhoneStateEvent.LogTermsOfService ->
                viewModelScope.launchSafe { logTermsOfService() }
            is EnterPhoneStateEvent.SetLegalCheckbox ->
                viewModelScope.launchSafe { setLegalCheckbox(stateEvent.isChecked) }
            is EnterPhoneStateEvent.SetCountryCode ->
                viewModelScope.launchSafe { setCountryNameCode(stateEvent.countryCode) }
        }
    }

}