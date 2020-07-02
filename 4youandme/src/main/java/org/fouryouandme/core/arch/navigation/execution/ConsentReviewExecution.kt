package org.fouryouandme.core.arch.navigation.execution

import org.fouryouandme.NavigationDirections
import org.fouryouandme.auth.consent.review.ConsentReviewFragmentDirections
import org.fouryouandme.auth.consent.review.info.ConsentReviewInfoFragmentDirections
import org.fouryouandme.core.arch.navigation.NavigationExecution

fun consentReviewInfoToConsentReviewDisagree(): NavigationExecution = {
    it.navigate(
        ConsentReviewInfoFragmentDirections.actionConsentReviewInfoToConsentReviewDisagree()
    )
}

fun consentReviewDisagreeToAuth(): NavigationExecution = {
    it.navigate(
        NavigationDirections.actionGlobalSplash()
    )
}

fun consentReviewToOptIns(): NavigationExecution = {
    it.navigate(
        ConsentReviewFragmentDirections.actionConsentReviewToOptIns()
    )
}