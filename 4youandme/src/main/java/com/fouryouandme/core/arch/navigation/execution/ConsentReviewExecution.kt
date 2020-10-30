package com.fouryouandme.core.arch.navigation.execution

import com.fouryouandme.NavigationDirections
import com.fouryouandme.auth.consent.review.ConsentReviewFragmentDirections
import com.fouryouandme.auth.consent.review.info.ConsentReviewInfoFragmentDirections
import com.fouryouandme.core.arch.navigation.NavigationExecution

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