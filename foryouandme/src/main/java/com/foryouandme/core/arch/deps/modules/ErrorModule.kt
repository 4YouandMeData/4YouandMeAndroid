package com.foryouandme.core.arch.deps.modules

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.toForYouAndMeError
import com.foryouandme.core.arch.error.unknownError
import com.foryouandme.entity.configuration.Text
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Response

data class ErrorModule(val moshi: Moshi)

suspend fun <T> ErrorModule.unwrapToEither(
    block: suspend () -> T
): Either<ForYouAndMeError, T> =
    unwrapToEither(null, block)

suspend fun <T> ErrorModule.unwrapToEither(
    text: Text?,
    block: suspend () -> T
): Either<ForYouAndMeError, T> =
    Either.catch(block).mapLeft { it.toForYouAndMeError(this, text) }

suspend fun <T> ErrorModule.unwrapResponse(
    response: Either<ForYouAndMeError, Response<T>>
): Either<ForYouAndMeError, T?> =
    response.flatMap {

        Either.catch {

            if (it.isSuccessful)
                it.body()
            else
                throw HttpException(it)

        }.mapLeft { it.toForYouAndMeError(this, null) }

    }

fun <T> Either<ForYouAndMeError, T?>.nullToError(
    error: ForYouAndMeError? = null
): Either<ForYouAndMeError, T> =
    fold(
        { it.left() },
        { it?.right() ?: (error ?: unknownError()).left() }
    )
