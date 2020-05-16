package org.fouryouandme.auth.phone

import arrow.core.None
import arrow.core.Option
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration

data class EnterPhoneState(
    val configuration: Option<Configuration> = None,
    val countryNameCode: Option<String> = None
)

sealed class EnterPhoneStateUpdate {
    data class Initialization(val configuration: Configuration) : EnterPhoneStateUpdate()
    data class CountryCode(val code: String) : EnterPhoneStateUpdate()
}

sealed class EnterPhoneLoading {
    object Initialization : EnterPhoneLoading()
    object PhoneNumberVerification : EnterPhoneLoading()
}

sealed class EnterPhoneError {
    object Initialization : EnterPhoneError()
    object PhoneNumberVerification : EnterPhoneError()
}

/* --- navigation --- */

data class EnterPhoneToPhoneValidationCode(
    val phone: String,
    val countryCode: String
) : NavigationAction

data class EnterPhoneToWeb(val url: String) : NavigationAction