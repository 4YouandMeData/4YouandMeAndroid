package com.foryouandme.ui.auth.phone

import com.foryouandme.core.arch.navigation.NavigationAction

data class EnterPhoneState(
    val countryNameCode: String? = null,
    val legalCheckbox: Boolean = false
)

sealed class EnterPhoneStateUpdate {
    data class CountryCode(val code: String) : EnterPhoneStateUpdate()
    data class LegalCheckBox(val legalCheckbox: Boolean) : EnterPhoneStateUpdate()
}

sealed class EnterPhoneLoading {
    object PhoneNumberVerification : EnterPhoneLoading()
}

sealed class EnterPhoneError {
    object PhoneNumberVerification : EnterPhoneError()
}

/* --- navigation --- */

data class EnterPhoneToPhoneValidationCode(
    val phone: String,
    val countryCode: String
) : NavigationAction
