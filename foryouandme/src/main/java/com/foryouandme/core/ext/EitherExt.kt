package com.foryouandme.core.ext

import arrow.core.Either
import arrow.core.EitherPartialOf
import arrow.core.computations.either
import arrow.typeclasses.suspended.BindSyntax
import com.foryouandme.core.arch.error.ForYouAndMeError

suspend fun <A> either.invokeAsForYouAndMeError(
    block: suspend BindSyntax<EitherPartialOf<ForYouAndMeError>>.() -> A
): Either<ForYouAndMeError, A> = invoke(block)