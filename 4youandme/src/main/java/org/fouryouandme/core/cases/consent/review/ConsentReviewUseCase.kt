package org.fouryouandme.core.cases.consent.review

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.consent.review.ConsentReviewRepository.getConsent
import org.fouryouandme.core.entity.consent.review.ConsentReview

object ConsentReviewUseCase {

    suspend fun ConsentReviewModule.getConsent(): Either<FourYouAndMeError, ConsentReview> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { getConsent(it, environment.studyId()) }
}