package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.*
import arrow.fx.typeclasses.ConcurrentSyntax
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.entity.configuration.Text

sealed class FourYouAndMeError(val message: (Context) -> String) {

    /* --- generic --- */

    class Unknown(message: String?) : FourYouAndMeError({
        message ?: it.getString(R.string.ERROR_generic) }
    )

    class NetworkErrorTimeOut(message: String?) : FourYouAndMeError({
        message ?: it.getString(R.string.ERROR_network_connection) }
    )

    class NetworkErrorUnknownHost(message: String?) : FourYouAndMeError({
        message ?: it.getString(R.string.ERROR_generic) }
    )

    data class NetworkErrorHTTP(
        val code: Int,
        val endpoint: Option<String>,
        val method: Option<String>,
        val token: Option<String>,
        val requestBody: Option<String>,
        val responseBody: Option<String>,
        val errorMessage: (Context) -> String = defaultNetworkErrorMessage(None)
    ) : FourYouAndMeError(errorMessage)

    /* --- auth --- */

    class MissingPhoneNumber(message: (Context) -> String): FourYouAndMeError(message)

    class WrongPhoneCode(message: (Context) -> String): FourYouAndMeError(message)
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

fun unknownError(text: Option<Text>) =
    FourYouAndMeError.Unknown(text.map { it.error.messageDefault }.orNull())