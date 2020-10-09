package org.fouryouandme.auth.consent.user

import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.consent.user.ConsentUser

data class ConsentUserState(
    val consent: ConsentUser,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = ""
)

sealed class ConsentUserStateUpdate {

    data class Initialization(val consent: ConsentUser) : ConsentUserStateUpdate()

    data class FirstName(val firstName: String) : ConsentUserStateUpdate()

    data class LastName(val lastName: String) : ConsentUserStateUpdate()

    data class Email(val email: String) : ConsentUserStateUpdate()

}

sealed class ConsentUserLoading {

    object Initialization : ConsentUserLoading()
    object CreateUser : ConsentUserLoading()
    object ConfirmEmail : ConsentUserLoading()
    object ResendConfirmationEmail : ConsentUserLoading()
    object UpdateUser : ConsentUserLoading()

}

sealed class ConsentUserError {

    object Initialization : ConsentUserError()
    object CreateUser : ConsentUserError()
    object ConfirmEmail : ConsentUserError()
    object ResendConfirmationEmail : ConsentUserError()
    object UpdateUser : ConsentUserError()

}

/* --- navigation --- */

object ConsentUserNameToConsentUserEmail : NavigationAction
object ConsentUserEmailToConsentUserEmailValidationCode : NavigationAction
object ConsentUserEmailValidationCodeToConsentUserSignature : NavigationAction
object ConsentUserSignatureToConsentUserSuccess : NavigationAction
object ConsentUserToIntegration : NavigationAction