package com.foryouandme.ui.compose.error

import com.foryouandme.R
import com.foryouandme.core.ext.toTextSource
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.source.TextSource
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.toForYouAndMeException(): ForYouAndMeException =
    when (this) {
        is ForYouAndMeException -> this
        is UnknownHostException ->
            ForYouAndMeException.NetworkErrorUnknownHost()
        is TimeoutException,
        is SocketTimeoutException ->
            ForYouAndMeException.NetworkErrorTimeOut()
        is HttpException ->
            ForYouAndMeException.NetworkHTTPException(code())
        else ->
            ForYouAndMeException.Unknown
    }

fun ForYouAndMeException.getMessage(configuration: Configuration?): TextSource =
    when (this) {
        is ForYouAndMeException.NetworkErrorUnknownHost ->
            R.string.ERROR_network_connection.toTextSource()
        is ForYouAndMeException.NetworkErrorTimeOut ->
            R.string.ERROR_network_timeout.toTextSource()
        is ForYouAndMeException.NetworkHTTPException ->
            R.string.ERROR_api.toTextSource(code)
        is ForYouAndMeException.MissingPhoneNumber ->
            configuration?.text
                ?.phoneVerification
                ?.error
                ?.errorMissingNumber
                .toTextSource(R.string.ERROR_generic)
        is ForYouAndMeException.WrongCode ->
            configuration?.text
                ?.phoneVerification
                ?.error
                ?.errorWrongCode
                .toTextSource(R.string.ERROR_generic)
        ForYouAndMeException.Unknown ->
            configuration?.text
                ?.error
                ?.messageDefault
                .toTextSource(R.string.ERROR_generic)
        ForYouAndMeException.MissingConfiguration,
        ForYouAndMeException.UserNotLoggedIn ->
            R.string.ERROR_generic.toTextSource()
    }