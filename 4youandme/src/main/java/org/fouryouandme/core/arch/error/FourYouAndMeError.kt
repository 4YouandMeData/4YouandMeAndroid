package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import arrow.fx.typeclasses.ConcurrentSyntax
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime

sealed class FourYouAndMeError(val message: (Context) -> String) {

    /* --- generic --- */

    object Unkonwn : FourYouAndMeError({ it.getString(R.string.ERROR_generic) })

    object NetworkErrorTimeOut :
        FourYouAndMeError({ it.getString(R.string.ERROR_network_connection) })

    object NetworkErrorUnknownHost :
        FourYouAndMeError({ it.getString(R.string.ERROR_network_connection) })

    data class NetworkErrorHTTP(
        val code: Int,
        val endpoint: Option<String>,
        val method: Option<String>,
        val token: Option<String>,
        val requestBody: Option<String>,
        val responseBody: Option<String>,
        val errorMessage: (Context) -> String = defaultNetworkErrorMessage()
    ) : FourYouAndMeError(errorMessage)
}

fun <F, A> ConcurrentSyntax<F>.toFourYouAndMeError(
    runtime: Runtime<F>,
    either: Either<Throwable, A>
): Kind<F, Either<FourYouAndMeError, A>> =
    runtime.fx.concurrent {
        !when (either) {
            is Either.Left -> either.a.toFourYouAndMeError(runtime).map { it.left() }
            is Either.Right -> just(either)
        }
    }