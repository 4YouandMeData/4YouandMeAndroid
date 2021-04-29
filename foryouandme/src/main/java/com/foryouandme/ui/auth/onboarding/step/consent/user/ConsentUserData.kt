package com.foryouandme.ui.auth.onboarding.step.consent.user

import android.graphics.Bitmap
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.consent.user.ConsentUser

data class ConsentUserState(
    val consent: ConsentUser? = null,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val isValidEmail: Boolean = false
)

sealed class ConsentUserStateUpdate {

    object GetConsentUser : ConsentUserStateUpdate()

    object FirstName : ConsentUserStateUpdate()

    object LastName : ConsentUserStateUpdate()

    object Email : ConsentUserStateUpdate()

}

sealed class ConsentUserLoading {

    object GetConsentUser : ConsentUserLoading()
    object CreateUser : ConsentUserLoading()
    object ConfirmEmail : ConsentUserLoading()
    object ResendConfirmationEmail : ConsentUserLoading()
    object UpdateUser : ConsentUserLoading()

}

sealed class ConsentUserError {

    object GetConsentUser : ConsentUserError()
    object CreateUser : ConsentUserError()
    object ConfirmEmail : ConsentUserError()
    object ResendConfirmationEmail : ConsentUserError()
    object UpdateUser : ConsentUserError()

}

sealed class ConsentUserAction {

    object GetConsentUser: ConsentUserAction()
    object CreateUser: ConsentUserAction()
    data class SetEmail(val email: String): ConsentUserAction()
    data class ConfirmEmail(val code: String): ConsentUserAction()
    object ResendEmail: ConsentUserAction()
    data class SetFirstName(val firstName: String): ConsentUserAction()
    data class SetLastName(val lastName: String): ConsentUserAction()
    data class UpdateUser(val signature: Bitmap): ConsentUserAction()

    object ConsentEmailViewed: ConsentUserAction()
    object ConsentEmailValidationViewed: ConsentUserAction()
    object ConsentUserNameViewed: ConsentUserAction()
    object ConsentUserSignatureViewed: ConsentUserAction()

}

/* --- navigation --- */

object ConsentUserNameToConsentUserEmail : NavigationAction
object ConsentUserEmailToConsentUserEmailValidationCode : NavigationAction
object ConsentUserEmailValidationCodeToConsentUserSignature : NavigationAction
object ConsentUserSignatureToConsentUserSuccess : NavigationAction
