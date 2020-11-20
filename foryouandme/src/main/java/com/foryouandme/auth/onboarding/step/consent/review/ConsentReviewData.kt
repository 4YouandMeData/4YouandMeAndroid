package com.foryouandme.auth.onboarding.step.consent.review

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.core.entity.consent.review.ConsentReview
import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ConsentReviewState(
    val consentReview: ConsentReview,
    val items: List<DroidItem<Any>>
)

sealed class ConsentReviewStateUpdate {

    data class Initialization(
        val consentReview: ConsentReview,
        val items: List<DroidItem<Any>>
    ) : ConsentReviewStateUpdate()

}

sealed class ConsentReviewLoading {

    object Initialization : ConsentReviewLoading()

}

sealed class ConsentReviewError {

    object Initialization : ConsentReviewError()

}

/* --- navigation --- */

object ConsentReviewInfoToConsentReviewDisagree : NavigationAction
object ConsentReviewDisagreeToAuth : NavigationAction
object ConsentReviewToOptIns : NavigationAction
