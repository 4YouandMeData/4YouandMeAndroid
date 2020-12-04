package com.foryouandme.core.cases.consent.review

import arrow.core.Either
import com.foryouandme.core.arch.deps.modules.ConsentReviewModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.entity.consent.review.ConsentReview

object ConsentReviewRepository {

    internal suspend fun ConsentReviewModule.getConsent(
        token: String,
        studyId: String
    ): Either<ForYouAndMeError, ConsentReview> =
        errorModule.unwrapToEither { api.getConsent(token, studyId) }
            .map { it.get().toConsentReview(it) }
            .nullToError()
}