package org.fouryouandme.core.ext

import arrow.core.Either
import arrow.core.EitherPartialOf
import arrow.core.computations.either
import arrow.typeclasses.suspended.BindSyntax
import org.fouryouandme.core.arch.error.FourYouAndMeError

suspend fun <A> either.invokeAsFourYouAndMeError(
    block: suspend BindSyntax<EitherPartialOf<FourYouAndMeError>>.() -> A
): Either<FourYouAndMeError, A> = invoke(block)