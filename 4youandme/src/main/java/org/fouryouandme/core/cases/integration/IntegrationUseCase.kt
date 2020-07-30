package org.fouryouandme.core.cases.integration

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.integration.Integration
import org.fouryouandme.core.ext.foldToKindEither

object IntegrationUseCase {

    fun <F> getIntegration(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Integration>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                IntegrationRepository.getIntegration(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )

            }

        }

}