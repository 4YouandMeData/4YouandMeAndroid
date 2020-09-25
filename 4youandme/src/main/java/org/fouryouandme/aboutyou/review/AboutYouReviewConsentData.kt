package org.fouryouandme.aboutyou.review

import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.review.ConsentReview

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