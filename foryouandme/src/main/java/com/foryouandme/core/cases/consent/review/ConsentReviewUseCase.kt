package com.foryouandme.core.cases.consent.review

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.ConsentReviewModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.consent.review.ConsentReviewRepository.getConsent
import com.foryouandme.core.entity.consent.review.ConsentReview

object ConsentReviewUseCase {

    suspend fun ConsentReviewModule.getConsent(): Either<ForYouAndMeError, ConsentReview> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { getConsent(it, environment.studyId()) }
}