package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.auth.onboarding.step.consent.user.email.ConsentUserEmailFragmentDirections
import com.foryouandme.auth.onboarding.step.consent.user.email.code.ConsentUserEmailValidationCodeFragmentDirections
import com.foryouandme.auth.onboarding.step.consent.user.name.ConsentUserNameFragmentDirections
import com.foryouandme.auth.onboarding.step.consent.user.signature.ConsentUserSignatureFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

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

