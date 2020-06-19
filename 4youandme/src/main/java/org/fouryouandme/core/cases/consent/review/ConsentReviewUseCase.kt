package org.fouryouandme.core.cases.consent.review

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
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

}