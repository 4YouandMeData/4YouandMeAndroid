package com.foryouandme.ui.aboutyou.review

import com.foryouandme.core.arch.LazyData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.consent.review.ConsentReview
import com.foryouandme.ui.compose.items.consent.ConsentReviewPageItem

data class ReviewConsentState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val consentReview: LazyData<ConsentReview> = LazyData.Empty,
    val items: List<ConsentReviewPageItem> = emptyList()
)

sealed class ReviewConsentAction {

    object GetConfiguration: ReviewConsentAction()
    object GetReviewConsent: ReviewConsentAction()

}