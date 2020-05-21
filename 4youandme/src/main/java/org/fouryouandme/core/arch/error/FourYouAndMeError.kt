package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.Either
import arrow.core.None
import arrow.core.Option
import arrow.core.left
import arrow.fx.typeclasses.ConcurrentSyntax
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.entity.configuration.Text

sealed class FourYouAndMeError(val title: (Context) -> String, val message: (Context) -> String) {

    /* --- generic --- */

    class Unknown(message: String?) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_generic) }
    )

    class NetworkErrorTimeOut(message: String?) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_network_connection) }
    )

    class NetworkErrorUnknownHost(message: String?) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_generic) }
    )

    data class NetworkErrorHTTP(
        val code: Int,
        val endpoint: Option<String>,
        val method: Option<String>,
        val token: Option<String>,
        val requestBody: Option<String>,
        val responseBody: Option<String>,
        val errorMessage: (Context) -> String = defaultNetworkErrorMessage(None)
    ) : FourYouAndMeError({ it.getString(R.string.ERROR_title) }, errorMessage)

    /* --- auth --- */

    class MissingPhoneNumber(message: (Context) -> String) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )

    class WrongPhoneCode(message: (Context) -> String) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )
}

fun <F, A> ConcurrentSyntax<F>.toFourYouAndMeError(
    runtime: Runtime<F>,
    either: Either<Throwable, A>,
    text: Option<Text>
): Kind<F, Either<FourYouAndMeError, A>> =
    runtime.fx.concurrent {
        !when (either) {
            is Either.Left -> either.a.toFourYouAndMeError(runtime, text).map { it.left() }
            is Either.Right -> just(either)
        }
    }

fun unknownError(): FourYouAndMeError.Unknown = unknownError(None)

fun unknownError(text: Option<Text>): FourYouAndMeError.Unknown =
    FourYouAndMeError.Unknown(text.map { it.error.messageDefault }.orNull())