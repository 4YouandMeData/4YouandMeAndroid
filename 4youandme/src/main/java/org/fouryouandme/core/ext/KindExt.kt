package org.fouryouandme.core.ext

import arrow.Kind
import arrow.core.*
import arrow.core.extensions.either.applicativeError.applicativeError
import arrow.fx.ForIO
import arrow.fx.fix
import arrow.fx.typeclasses.ConcurrentFx
import arrow.fx.typeclasses.Disposable
import arrow.integrations.retrofit.adapter.unwrapBody
import arrow.typeclasses.ApplicativeError
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.toFourYouAndMeError
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.configuration.ConfigurationUseCase
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

fun <A> Kind<ForIO, A>.unsafeRunAsync(): Unit =
    fix().unsafeRunAsync { either ->
        either.mapLeft { Timber.tag("4_YOU_AND_ME_ERROR").e(it) }
    }

fun <A> Kind<ForIO, A>.unsafeRunAsyncCancelable(): Disposable =
    fix().unsafeRunAsyncCancellable { either ->
        either.mapLeft { Timber.tag("4_YOU_AND_ME_ERROR").e(it) }
    }

fun <A> Kind<ForIO, A>.unsafeRunSync(): A = fix().unsafeRunSync()

fun <F, A> Kind<F, Either<Throwable, Response<A>>>.unwrapToEither(
    runtime: Runtime<F>
): Kind<F, Either<FourYouAndMeError, A>> =
    runtime.fx.concurrent {

        val request =
            !this@unwrapToEither

        val unwrappedResponse =
            request.flatMap { it.unwrapBody(Either.applicativeError()).fix() }

        val configuration =
            ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                .bind()
                .toOption()

        !toFourYouAndMeError(runtime, unwrappedResponse, configuration.map { it.text })
    }

fun <F, A> Kind<F, Either<Throwable, Response<A>>>.unwrapEmptyToEither(
    runtime: Runtime<F>
): Kind<F, Either<FourYouAndMeError, Unit>> =
    runtime.fx.concurrent {

        val request =
            !this@unwrapEmptyToEither

        val unwrappedResponse =
            request.flatMap { it.unwrapEmptyBody(Either.applicativeError()).fix() }

        val configuration =
            ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                .bind()
                .toOption()

        !toFourYouAndMeError(runtime, unwrappedResponse, configuration.map { it.text })
    }

fun <F, R> Response<R>.unwrapEmptyBody(apError: ApplicativeError<F, Throwable>): Kind<F, Unit> =
    if (this.isSuccessful)
        apError.just(Unit)
    else
        apError.raiseError(HttpException(this))

/* --- fold ext --- */

fun <F, E, A, B> Either<E, A>.foldToKindEither(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> Kind<F, Either<E, B>>
): Kind<F, Either<E, B>> =
    foldToKindEither(fx, valueMapper) { it }

fun <F, E, E2, A, B> Either<E, A>.foldToKindEither(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> Kind<F, Either<E2, B>>,
    errorMapper: (E) -> E2
): Kind<F, Either<E2, B>> =
    fx.concurrent {

        this@foldToKindEither
            .fold(
                { just(errorMapper(it).left()) },
                { valueMapper(it) }
            )
            .bind()
    }


fun <F, E, A, B> Either<E, A>.foldToKind(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> Kind<F, B>
): Kind<F, Either<E, B>> =
    foldToKind(fx, valueMapper) { it }

fun <F, E, E2, A, B> Either<E, A>.foldToKind(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> Kind<F, B>,
    errorMapper: (E) -> E2
): Kind<F, Either<E2, B>> =
    fx.concurrent {

        !this@foldToKind
            .fold(
                { just(errorMapper(it).left()) },
                { value -> valueMapper(value).map { it.right() } }
            )
    }

/* --- mapping ext --- */

fun <F, E, E2, A> Kind<F, Either<E, A>>.mapError(
    fx: ConcurrentFx<F>,
    errorMapper: (E) -> E2
): Kind<F, Either<E2, A>> =
    mapResult(fx, { it }, errorMapper)

fun <F, E, A, B> Kind<F, Either<E, A>>.mapResult(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> B
): Kind<F, Either<E, B>> =
    mapResult(fx, valueMapper) { it }

fun <F, E, E2, A, B> Kind<F, Either<E, A>>.mapResult(
    fx: ConcurrentFx<F>,
    valueMapper: (A) -> B,
    errorMapper: (E) -> E2
): Kind<F, Either<E2, B>> =
    fx.concurrent {

        val source = !this@mapResult

        source.fold({ errorMapper(it).left() }, { valueMapper(it).right() })
    }


/* --- none to error --- */

fun <F, A> Kind<F, Either<FourYouAndMeError, Option<A>>>.noneToError(
    runtime: Runtime<F>,
    error: FourYouAndMeError? = null
): Kind<F, Either<FourYouAndMeError, A>> =
    runtime.fx.concurrent {

        !this@noneToError.bind().noneToError(runtime, error)

    }

fun <F, A> Either<FourYouAndMeError, Option<A>>.noneToError(
    runtime: Runtime<F>,
    error: FourYouAndMeError? = null
): Kind<F, Either<FourYouAndMeError, A>> =
    runtime.fx.concurrent {

        val configuration =
            ConfigurationUseCase.getConfiguration(runtime, CachePolicy.MemoryOrDisk)
                .bind()
                .toOption()

        this@noneToError.flatMap { option ->
            option.fold(
                { (error ?: unknownError(configuration.map { it.text })).left() },
                { it.right() }
            )
        }

    }
