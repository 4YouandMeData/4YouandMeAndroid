package org.fouryouandme.core.cases.wearable

import arrow.Kind
import arrow.core.Either
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.wearable.Wearable
import org.fouryouandme.core.ext.foldToKindEither

object WearableUseCase {

    fun <F> getWearable(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Wearable>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {

                WearableRepository.getWearable(
                    runtime,
                    it,
                    runtime.injector.environment.studyId()
                )

            }

        }

}