package com.fouryouandme.core.cases.consent.review

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.consent.review.ConsentReviewRepository.getConsent
import com.fouryouandme.core.entity.consent.review.ConsentReview

object ConsentReviewUseCase {

    suspend fun ConsentReviewModule.getConsent(): Either<FourYouAndMeError, ConsentReview> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { getConsent(it, environment.studyId()) }
}