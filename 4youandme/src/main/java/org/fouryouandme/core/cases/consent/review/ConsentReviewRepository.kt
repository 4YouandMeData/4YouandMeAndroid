package org.fouryouandme.core.cases.consent.review

import arrow.core.Either
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.deps.modules.nullToError
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.consent.review.ConsentReview

object ConsentReviewRepository {

    internal suspend fun ConsentReviewModule.getConsent(
        token: String,
        studyId: String
    ): Either<FourYouAndMeError, ConsentReview> =
        errorModule.unwrapToEither { api.getConsent(token, studyId) }
            .map { it.get().toConsentReview(it) }
            .nullToError()
}