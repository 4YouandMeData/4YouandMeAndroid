package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.*
import com.squareup.moshi.Json
import okhttp3.RequestBody
import okio.Buffer
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

private fun <F> HttpException.toNetworkErrorHTTP(
    runtime: Runtime<F>
): Kind<F, FourYouAndMeError.NetworkErrorHTTP> =
    runtime.fx.concurrent {

        val raw = response()?.raw()
        val request = raw?.request.toOption()
        val response = response().toOption()

        val requestJson =
            !when (request) {
                None -> just(None)
                is Some ->
                    request.t.body.bodyToString(runtime)
                        .map { either ->
                            either.toOption().flatMap { it }
                        }
            }

        val responseJson =
            response.flatMap { it.errorBody()?.string().toOption() }


        val errorMessage = !responseJson.parseErrorMessage(runtime)

        FourYouAndMeError.NetworkErrorHTTP(
            code(),
            request.map { it.url.toString() },
            request.map { it.method },
            request.flatMap { it.headers["Authorization"].toOption() },
            requestJson,
            responseJson,
            errorMessage
        )
    }

private fun <F> RequestBody?.bodyToString(
    runtime: Runtime<F>
): Kind<F, Either<Throwable, Option<String>>> =
    runtime.fx.concurrent {
        !effect {
            this@bodyToString
                .toOption()
                .map {
                    val buffer = Buffer()
                    it.writeTo(buffer)
                    buffer.readUtf8()
                }
        }.attempt()
    }

internal fun defaultNetworkErrorMessage(): (Context) -> String =
    { it.getString(R.string.ERROR_api) }

data class ServerErrorMessage(@Json(name = "message") val messageCode: String)

private fun <F> Option<String>.parseErrorMessage(
    runtime: Runtime<F>
): Kind<F, (Context) -> String> =

    map {
        runtime.fx.concurrent {

            runtime.injector
                .moshi
                .adapter(ServerErrorMessage::class.java)
                .fromJson(it)
                .toOption()
        }
    }.map { parser ->
        runtime.fx.concurrent {

            val result = !parser.attempt()

            result.fold(
                { defaultNetworkErrorMessage() },
                { option ->
                    option.fold(
                        { defaultNetworkErrorMessage() },
                        { it.mapToMessage() })
                }
            )
        }
    }.getOrElse { runtime.fx.concurrent { defaultNetworkErrorMessage() } }


private fun ServerErrorMessage.mapToMessage(): (Context) -> String = defaultNetworkErrorMessage()

fun <F> Throwable.toFourYouAndMeError(runtime: Runtime<F>): Kind<F, FourYouAndMeError> =
    runtime.fx.concurrent {
        when (this@toFourYouAndMeError) {
            is UnknownHostException -> FourYouAndMeError.NetworkErrorUnknownHost
            is TimeoutException,
            is SocketTimeoutException -> FourYouAndMeError.NetworkErrorTimeOut
            is HttpException -> !this@toFourYouAndMeError.toNetworkErrorHTTP(runtime)
            else -> FourYouAndMeError.Unkonwn
        }
    }
