package com.foryouandme.ui.auth.signin.phone

import com.foryouandme.core.arch.navigation.NavigationAction

data class EnterPhoneState(
    val countryNameCode: String? = null,
    val legalCheckbox: Boolean = false
)

sealed class EnterPhoneLoading {
    object PhoneNumberVerification : EnterPhoneLoading()
}

sealed class EnterPhoneError {
    object PhoneNumberVerification : EnterPhoneError()
}

sealed class EnterPhoneStateEvent {
    data class VerifyPhoneNumber(
        val phoneAndCode: String,
        val phone: String,
        val countryCode: String
    ) : EnterPhoneStateEvent()
    data class SetLegalCheckbox(val isChecked: Boolean) : EnterPhoneStateEvent()
    data class SetCountryCode(val countryCode: String) : EnterPhoneStateEvent()
    object ScreenViewed : EnterPhoneStateEvent()
    object LogPrivacyPolicy : EnterPhoneStateEvent()
    object LogTermsOfService : EnterPhoneStateEvent()
}

/* --- navigation --- */

data class EnterPhoneToPhoneValidationCode(
    val phone: String,
    val countryCode: String
) : NavigationAction
