package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.auth.consent.user.ConsentUserFragmentDirections
import org.fouryouandme.auth.consent.user.email.ConsentUserEmailFragmentDirections
import org.fouryouandme.auth.consent.user.email.code.ConsentUserEmailValidationCodeFragmentDirections
import org.fouryouandme.auth.consent.user.name.ConsentUserNameFragmentDirections
import org.fouryouandme.auth.consent.user.signature.ConsentUserSignatureFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentUserNameToConsentUserEmail(): NavigationExecution = {
    it.navigate(ConsentUserNameFragmentDirections.actionConsentUserNameToConsentUserEmail())
}

fun consentUserEmailToConsentUserEmailValidationCode(): NavigationExecution = {
    it.navigate(
        ConsentUserEmailFragmentDirections.actionConsentUserEmailToConsentUserEmailValidationCode()
    )
}

fun consentUserEmailValidationCodeToConsentUserSignature(): NavigationExecution = {
    it.navigate(
        ConsentUserEmailValidationCodeFragmentDirections.actionConsentUserEmailValidationCodeToConsentUserSignature()
    )
}

fun consentUserSignatureToConsentUserSuccess(): NavigationExecution = {
    it.navigate(
        ConsentUserSignatureFragmentDirections.actionConsentUserSignatureToConsentUserSuccess()
    )
}

fun consentUserToWearable(): NavigationExecution = {
    it.navigate(
        ConsentUserFragmentDirections.actionConsentUserToWearable()
    )
}