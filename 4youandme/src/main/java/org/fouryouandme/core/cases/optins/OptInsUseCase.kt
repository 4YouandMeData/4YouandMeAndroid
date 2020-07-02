package org.fouryouandme.core.cases.optins

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase
import org.fouryouandme.core.entity.optins.OptIns
import org.fouryouandme.core.ext.foldToKindEither

object OptInsUseCase {

    fun <F> getOptIns(
        runtime: Runtime<F>
    ): Kind<F, Either<FourYouAndMeError, Option<OptIns>>> =
        runtime.fx.concurrent {

            val token =
                !AuthUseCase.getToken(runtime, CachePolicy.MemoryFirst)

            !token.foldToKindEither(runtime.fx) {
                OptInsRepository.getOptIns(runtime, it)
            }

        }
}