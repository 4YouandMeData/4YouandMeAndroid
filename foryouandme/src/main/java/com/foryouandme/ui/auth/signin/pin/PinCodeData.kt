package com.foryouandme.ui.auth.signin.pin

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration

data class PinCodeState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val legalCheckbox: Boolean = false,
    val pin: String = "",
    val isPinValid: Boolean = false,
    val auth: LazyData<Unit> = LazyData.Empty,
)

sealed class PinCodeLoading {
    object Auth : PinCodeLoading()
}

sealed class PinCodeError {
    object Auth : PinCodeError()
}

sealed class PinCodeAction {
    object GetConfiguration: PinCodeAction()
    object Auth : PinCodeAction()
    data class SetLegalCheckbox(val isChecked: Boolean) : PinCodeAction()
    data class SetPin(val pin: String): PinCodeAction()
    object ScreenViewed : PinCodeAction()
    object LogPrivacyPolicy : PinCodeAction()
    object LogTermsOfService : PinCodeAction()
}

sealed class PinCodeEvent {
    object Main : PinCodeEvent()
    object Onboarding : PinCodeEvent()
    data class AuthError(val error: ForYouAndMeException) : PinCodeEvent()
}

/* --- navigation --- */

object PinCodeToOnboarding : NavigationAction
object PinCodeToMain : NavigationAction
