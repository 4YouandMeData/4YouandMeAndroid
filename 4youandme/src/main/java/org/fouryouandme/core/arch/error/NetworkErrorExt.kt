package org.fouryouandme.core.arch.error

import android.content.Context
import arrow.Kind
import arrow.core.*
import arrow.fx.coroutines.evalOn
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody
import okio.Buffer
import org.fouryouandme.R
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.deps.modules.ErrorModule
import org.fouryouandme.core.entity.configuration.Text
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

@Deprecated("use the suspend version")
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
            request.map { it.url.toString() }.orNull(),
            request.map { it.method }.orNull(),
            request.flatMap { it.headers["Authorization"].toOption() }.orNull(),
            requestJson.orNull(),
            responseJson.orNull(),
            errorMessage
        )
    }

private suspend fun HttpException.toNetworkErrorHTTP(
    errorModule: ErrorModule,
    text: Text?
): FourYouAndMeError.NetworkErrorHTTP =
    evalOn(Dispatchers.IO) {

        val raw = response()?.raw()
        val request = raw?.request
        val response = response()

        val requestJson =
            request?.body.bodyToString().orNull()

        val responseJson =
            response?.errorBody()?.string()


        val errorMessage = responseJson.parseErrorMessage(errorModule, text)

        FourYouAndMeError.NetworkErrorHTTP(
            code(),
            request?.url.toString(),
            request?.method,
            request?.headers?.get("Authorization"),
            requestJson,
            responseJson,
            errorMessage
        )

    }

@Deprecated("use the suspend version")
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

private suspend fun RequestBody?.bodyToString(): Either<Throwable, String?> =

    Either.catch {

        evalOn(Dispatchers.IO) { // ensure that run on IO dispatcher

            this?.let {
                val buffer = Buffer()
                it.writeTo(buffer)
                buffer.readUtf8()
            }

        }

    }

internal fun defaultNetworkErrorMessage(text: Text?): (Context) -> String =
    { text?.error?.messageDefault ?: it.getString(R.string.ERROR_api) }

data class ServerErrorMessage(@Json(name = "message") val messageCode: String)

@Deprecated("use the suspend verison")
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
                { defaultNetworkErrorMessage(text.orNull()) },
                { option ->
                    option.fold(
                        { defaultNetworkErrorMessage(text.orNull()) },
                        { it.mapToMessage(text.orNull()) })
                }
            )
        }
    }.getOrElse { runtime.fx.concurrent { defaultNetworkErrorMessage(text.orNull()) } }


private suspend fun String?.parseErrorMessage(
    errorModule: ErrorModule,
    text: Text?
): ((Context) -> String) =
    this?.let {

        val error = parseServerErrorMessage(errorModule)

        error.fold(
            { defaultNetworkErrorMessage(text) },
            { it?.mapToMessage(text) ?: defaultNetworkErrorMessage(text) }
        )

    } ?: defaultNetworkErrorMessage(text)

private suspend fun String.parseServerErrorMessage(
    errorModule: ErrorModule
): Either<Throwable, ServerErrorMessage?> =
    Either.catch {

        evalOn(Dispatchers.IO) { // ensure that run on IO dispatcher

            errorModule
                .moshi
                .adapter(ServerErrorMessage::class.java)
                .fromJson(this)

        }
    }

private fun ServerErrorMessage.mapToMessage(text: Text?): (Context) -> String =
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

suspend fun Throwable.toFourYouAndMeError(
    errorModule: ErrorModule,
    text: Text?
): FourYouAndMeError =
    when (this) {
        is UnknownHostException ->
            FourYouAndMeError.NetworkErrorUnknownHost(text?.error?.messageConnectivity)
        is TimeoutException,
        is SocketTimeoutException ->
            FourYouAndMeError.NetworkErrorTimeOut(text?.error?.messageConnectivity)
        is HttpException ->
            toNetworkErrorHTTP(errorModule, text)
        else ->
            FourYouAndMeError.Unknown(text?.error?.messageDefault)
    }

