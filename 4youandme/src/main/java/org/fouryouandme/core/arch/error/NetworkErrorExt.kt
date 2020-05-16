package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.*
import com.squareup.moshi.Json
import okhttp3.RequestBody
import okio.Buffer
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.entity.configuration.Text
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

private fun <F> HttpException.toNetworkErrorHTTP(
    runtime: Runtime<F>,
    text: Option<Text>
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


        val errorMessage = !responseJson.parseErrorMessage(runtime, text)

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

internal fun defaultNetworkErrorMessage(text: Option<Text>): (Context) -> String =
    { text.map { it.error.messageDefault }.getOrElse { it.getString(R.string.ERROR_api) } }

data class ServerErrorMessage(@Json(name = "message") val messageCode: String)

private fun <F> Option<String>.parseErrorMessage(
    runtime: Runtime<F>,
    text: Option<Text>
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
                { defaultNetworkErrorMessage(text) },
                { option ->
                    option.fold(
                        { defaultNetworkErrorMessage(text) },
                        { it.mapToMessage(text) })
                }
            )
        }
    }.getOrElse { runtime.fx.concurrent { defaultNetworkErrorMessage(text) } }


private fun ServerErrorMessage.mapToMessage(text: Option<Text>): (Context) -> String =
    defaultNetworkErrorMessage(text)

fun <F> Throwable.toFourYouAndMeError(
    runtime: Runtime<F>,
    text: Option<Text>
): Kind<F, FourYouAndMeError> =
    runtime.fx.concurrent {
        when (this@toFourYouAndMeError) {
            is UnknownHostException ->
                FourYouAndMeError.NetworkErrorUnknownHost(
                    text.map { it.error.messageConnectivity }.orNull()
                )
            is TimeoutException,
            is SocketTimeoutException ->
                FourYouAndMeError.NetworkErrorTimeOut(
                    text.map { it.error.messageConnectivity }.orNull()
                )
            is HttpException -> !this@toFourYouAndMeError.toNetworkErrorHTTP(runtime, text)
            else ->
                FourYouAndMeError.Unknown(
                    text.map { it.error.messageDefault }.orNull()
                )
        }
    }
