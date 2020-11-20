package com.foryouandme.core.arch.navigation.execution

import com.foryouandme.NavigationDirections
import com.foryouandme.auth.onboarding.step.consent.review.ConsentReviewFragmentDirections
import com.foryouandme.auth.onboarding.step.consent.review.info.ConsentReviewInfoFragmentDirections
import com.foryouandme.core.arch.navigation.NavigationExecution

fun consentReviewInfoToConsentReviewDisagree(): NavigationExecution = {
    it.navigate(
        ConsentReviewInfoFragmentDirections.actionConsentReviewInfoToConsentReviewDisagree()
    )
}

fun consentReviewDisagreeToAuth(): NavigationExecution = {
    it.navigate(
        NavigationDirections.actionGlobalAuth()
    )
}

fun consentReviewToOptIns(): NavigationExecution = {
    it.navigate(
        ConsentReviewFragmentDirections.actionConsentReviewToOptIn()
    )
}