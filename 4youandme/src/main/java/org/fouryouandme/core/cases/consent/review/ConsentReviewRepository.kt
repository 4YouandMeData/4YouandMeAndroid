package org.fouryouandme.core.cases.consent.review

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.consent.review.ConsentReview
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapToEither

object ConsentReviewRepository {

    internal fun <F> getConsent(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, ConsentReview>> =
        runtime.fx.concurrent {

            !runtime.injector.consentReviewApi.getConsent(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get().toConsentReview(it) }
                .noneToError(runtime)

        }

}