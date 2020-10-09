package org.fouryouandme.auth.consent.review

import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.consent.review.ConsentReview

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
