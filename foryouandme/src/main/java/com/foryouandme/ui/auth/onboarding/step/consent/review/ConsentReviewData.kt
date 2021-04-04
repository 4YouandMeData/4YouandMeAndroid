package com.foryouandme.ui.auth.onboarding.step.consent.review

import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.review.ConsentReview
import com.giacomoparisi.recyclerdroid.core.DroidItem

data class ConsentReviewState(
    val consentReview: ConsentReview? = null,
    val items: List<DroidItem<Any>> = emptyList()
)

sealed class ConsentReviewStateUpdate {

    object ConsentReview : ConsentReviewStateUpdate()

}

sealed class ConsentReviewLoading {

    object ConsentReview : ConsentReviewLoading()

}

sealed class ConsentReviewError {

    object ConsentReview : ConsentReviewError()

}

sealed class ConsentReviewStateEvent {

    data class GetConsentReview(val configuration: Configuration) : ConsentReviewStateEvent()
    object Agree: ConsentReviewStateEvent()
    object Disagree: ConsentReviewStateEvent()

}

/* --- navigation --- */

object ConsentReviewInfoToConsentReviewDisagree : NavigationAction
object ConsentReviewDisagreeToAuth : NavigationAction
object ConsentReviewToOptIns : NavigationAction
