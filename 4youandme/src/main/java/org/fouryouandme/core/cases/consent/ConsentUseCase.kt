package org.fouryouandme.core.cases.consent

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.consent.Consent
import org.fouryouandme.core.ext.foldToKindEither

object ConsentUseCase {

    fun <F> getConsent(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Consent>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)


            !token.foldToKindEither(runtime.fx) {
                ConsentRepository.getConsent(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )
            }

        }

}