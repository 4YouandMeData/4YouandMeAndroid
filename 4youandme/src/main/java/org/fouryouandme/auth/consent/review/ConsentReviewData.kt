package org.fouryouandme.auth.consent.review

import arrow.core.None
import arrow.core.Option
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.navigation.NavigationAction
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.consent.review.ConsentReview

data class ConsentReviewState(
    val configuration: Option<Configuration> = None,
    val consentReview: Option<ConsentReview> = None,
    val items: List<DroidItem<Any>> = emptyList()
)

sealed class ConsentReviewStateUpdate {

    data class Initialization(
        val configuration: Configuration,
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

object ConsentReviewInfoToConsentReviewDisagree: NavigationAction
object ConsentReviewDisagreeToAuth : NavigationAction
object ConsentReviewToOptIns : NavigationAction
