package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.consent.user.email.ConsentUserEmailFragmentDirections
import org.fouryouandme.auth.consent.user.name.ConsentUserNameFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentUserNameToConsentUserEmail(): NavigationExecution = {
    it.navigate(ConsentUserNameFragmentDirections.actionConsentUserNameToConsentUserEmail())
}

fun consentUserEmailToConsentUserEmailVerificationCode(): NavigationExecution = {
    it.navigate(
        ConsentUserEmailFragmentDirections.actionConsentUserEmailToConsentUserEmailValidationCode()
    )
}