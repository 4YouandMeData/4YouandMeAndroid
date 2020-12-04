package com.foryouandme.core.arch.error

import android.content.Context
import arrow.core.Either
import arrow.fx.coroutines.evalOn
import com.foryouandme.R
import com.foryouandme.core.arch.deps.modules.ErrorModule
import com.foryouandme.entity.configuration.Text
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import okhttp3.RequestBody
import okio.Buffer
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

private suspend fun HttpException.toNetworkErrorHTTP(
    errorModule: ErrorModule,
    text: Text?
): ForYouAndMeError.NetworkErrorHTTP =
    evalOn(Dispatchers.IO) {

        val raw = response()?.raw()
        val request = raw?.request
        val response = response()

        val requestJson =
            request?.body.bodyToString().orNull()

        val responseJson =
            response?.errorBody()?.string()


        val errorMessage = responseJson.parseErrorMessage(errorModule, text)

        ForYouAndMeError.NetworkErrorHTTP(
            code(),
            request?.url.toString(),
            request?.method,
            request?.headers?.get("Authorization"),
            requestJson,
            responseJson,
            errorMessage
        )

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

suspend fun Throwable.toForYouAndMeError(
    errorModule: ErrorModule,
    text: Text?
): ForYouAndMeError =
    when (this) {
        is UnknownHostException ->
            ForYouAndMeError.NetworkErrorUnknownHost(text?.error?.messageConnectivity)
        is TimeoutException,
        is SocketTimeoutException ->
            ForYouAndMeError.NetworkErrorTimeOut(text?.error?.messageConnectivity)
        is HttpException ->
            toNetworkErrorHTTP(errorModule, text)
        else ->
            ForYouAndMeError.Unknown(text?.error?.messageDefault)
    }

