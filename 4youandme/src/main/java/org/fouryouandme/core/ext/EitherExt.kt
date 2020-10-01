package org.fouryouandme.core.ext

import arrow.Kind
import arrow.core.*
import arrow.core.computations.either
import arrow.fx.typeclasses.ConcurrentFx
import arrow.typeclasses.suspended.BindSyntax
import org.fouryouandme.core.arch.error.FourYouAndMeError

fun <F, E, A> Either<E, Kind<F, Either<E, A>>>.accumulateError(
    fx: ConcurrentFx<F>
): Kind<F, Either<E, A>> =
    fx.concurrent {

        !this@accumulateError.fold({ just(it.left()) }, { it })

    }

fun <E, A> Either<E, Option<A>>.noneToError(error: E): Either<E, A> =
    flatMap { it.toEither { error } }


suspend fun <A> either.invokeAsFourYouAndMeError(
    block: suspend BindSyntax<EitherPartialOf<FourYouAndMeError>>.() -> A
): Either<FourYouAndMeError, A> = invoke(block)