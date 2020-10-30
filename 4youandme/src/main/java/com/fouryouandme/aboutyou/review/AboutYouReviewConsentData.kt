package com.fouryouandme.aboutyou.review

import com.fouryouandme.core.entity.consent.review.ConsentReview
import com.giacomoparisi.recyclerdroid.core.DroidItem

data class AboutYouReviewConsentState(
    val consentReview: ConsentReview,
    val items: List<DroidItem<Any>> = emptyList()
)

sealed class AboutYouReviewConsentStateUpdate {

    data class Initialization(
        val consentReview: ConsentReview,
        val items: List<DroidItem<Any>>
    ) : AboutYouReviewConsentStateUpdate()

}

sealed class AboutYouReviewConsentLoading {

    object Initialization : AboutYouReviewConsentLoading()

}

sealed class AboutYouReviewConsentError {

    object Initialization : AboutYouReviewConsentError()

}