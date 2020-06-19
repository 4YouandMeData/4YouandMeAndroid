package org.fouryouandme.core.cases.consent.informed

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.consent.informed.ConsentInfo
import org.fouryouandme.core.ext.mapResult
import org.fouryouandme.core.ext.noneToError
import org.fouryouandme.core.ext.unwrapToEither

object ConsentInfoRepository {

    internal fun <F> getConsent(
        runtime: Runtime<F>,
        token: String,
        studyId: String
    ): Kind<F, Either<FourYouAndMeError, ConsentInfo>> =
        runtime.fx.concurrent {

            !runtime.injector.consentInfoApi.getConsent(token, studyId)
                .async(runtime.fx.M)
                .attempt()
                .unwrapToEither(runtime)
                .mapResult(runtime.fx) { it.get().toConsent(it) }
                .noneToError(runtime)

        }

}