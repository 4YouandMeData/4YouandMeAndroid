package com.fouryouandme.core.cases.consent.review

import arrow.core.Either
import com.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import com.fouryouandme.core.arch.deps.modules.nullToError
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.consent.review.ConsentReview

object ConsentReviewRepository {

    internal suspend fun ConsentReviewModule.getConsent(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, ConsentReview> =
        errorModule.unwrapToEither { api.getConsent(token, studyId) }
            .map { it.get().toConsentReview(it) }
            .nullToError()
}