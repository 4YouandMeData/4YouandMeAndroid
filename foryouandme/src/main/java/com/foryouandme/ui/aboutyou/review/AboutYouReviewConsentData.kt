package com.foryouandme.ui.aboutyou.review

import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.entity.page.Page
import com.foryouandme.ui.compose.items.consent.ConsentReviewPageItem
import com.giacomoparisi.recyclerdroid.core.DroidItem

data class AboutYouReviewConsentState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val consentReview: LazyData<ConsentReview> = LazyData.Empty,
    val items: List<ConsentReviewPageItem> = emptyList()
)

sealed class AboutYouReviewConsentAction {

    object GetConfiguration: AboutYouReviewConsentAction()
    object GetReviewConsent: AboutYouReviewConsentAction()

}