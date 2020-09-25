package org.fouryouandme.core.cases.consent.review

import arrow.Kind
import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ConfigurationModule
import org.fouryouandme.core.arch.deps.modules.ConsentReviewModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.consent.review.ConsentReviewRepository.getConsent
import org.fouryouandme.core.entity.consent.review.ConsentReview
import org.fouryouandme.core.ext.foldToKindEither

object ConsentReviewUseCase {

    fun <F> getConsent(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, ConsentReview>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)


            !token.foldToKindEither(runtime.fx) {
                ConsentReviewRepository.getConsent(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )
            }

        }

    suspend fun ConsentReviewModule.getConsent(): Either<FourYouAndMeError, ConsentReview> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { getConsent(it, environment.studyId()) }
}