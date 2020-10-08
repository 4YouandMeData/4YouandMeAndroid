package org.fouryouandme.core.arch.deps.modules

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.toFourYouAndMeError
import org.fouryouandme.core.arch.error.unknownError
import org.fouryouandme.core.entity.configuration.Text
import retrofit2.HttpException
import retrofit2.Response

data class ErrorModule(val moshi: Moshi)

suspend fun <T> ErrorModule.unwrapToEither(
    block: suspend () -> T
): Either<FourYouAndMeError, T> =
    unwrapToEither(null, block)

suspend fun <T> ErrorModule.unwrapToEither(
    text: Text?,
    block: suspend () -> T
): Either<FourYouAndMeError, T> =
    Either.catch(block).mapLeft { it.toFourYouAndMeError(this, text) }

suspend fun <T> ErrorModule.unwrapResponse(
    response: Either<FourYouAndMeError, Response<T>>
): Either<FourYouAndMeError, T?> =
    response.flatMap {

        Either.catch {

            if (it.isSuccessful)
                it.body()
            else
                throw HttpException(it)

        }.mapLeft { it.toFourYouAndMeError(this, null) }

    }

fun <T> Either<FourYouAndMeError, T?>.nullToError(
    error: FourYouAndMeError? = null
): Either<FourYouAndMeError, T> =
    fold(
        { it.left() },
        { it?.right() ?: (error ?: unknownError()).left() }
    )
