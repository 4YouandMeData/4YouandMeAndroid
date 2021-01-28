package com.foryouandme.core.arch.error

import com.foryouandme.domain.error.ForYouAndMeException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {

    fun mapToForYouAndMeException(throwable: Throwable): ForYouAndMeException =
        when (throwable) {
            is ForYouAndMeException -> throwable
            is UnknownHostException ->
                ForYouAndMeException.NetworkErrorUnknownHost()
            is TimeoutException,
            is SocketTimeoutException ->
                ForYouAndMeException.NetworkErrorTimeOut()
            is HttpException ->
                ForYouAndMeException.UserNotLoggedIn
            else ->
                ForYouAndMeException.Unknown
        }

}