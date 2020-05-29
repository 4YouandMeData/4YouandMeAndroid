package org.fouryouandme.core.cases.screening

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.screening.Screening
import org.fouryouandme.core.ext.foldToKindEither

object ScreeningUseCase {

    fun <F> getScreening(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Screening>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)


            !token.foldToKindEither(runtime.fx) {
                ScreeningRepository.getScreening(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )
            }

        }

}