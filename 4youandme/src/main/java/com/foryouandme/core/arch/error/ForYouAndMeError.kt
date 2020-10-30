package com.foryouandme.core.arch.error

import android.content.Context
import com.foryouandme.R
import com.foryouandme.core.entity.configuration.Text

sealed class ForYouAndMeError(val title: (Context) -> String, val message: (Context) -> String) {

    /* --- generic --- */

    object MissingConfiguration : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { it.getString(R.string.ERROR_generic) }
    )

    class Unknown(message: String?) : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_generic) }
    )

    class NetworkErrorTimeOut(message: String?) : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_network_connection) }
    )

    class NetworkErrorUnknownHost(message: String?) : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { message ?: it.getString(R.string.ERROR_generic) }
    )

    data class NetworkErrorHTTP(
        val code: Int,
        val endpoint: String?,
        val method: String?,
        val token: String?,
        val requestBody: String?,
        val responseBody: String?,
        val errorMessage: (Context) -> String = defaultNetworkErrorMessage(null)
    ) : ForYouAndMeError({ it.getString(R.string.ERROR_title) }, errorMessage)

    /* --- auth --- */

    object UserNotLoggedIn : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { it.getString(R.string.ERROR_generic) }
    )

    class MissingPhoneNumber(message: (Context) -> String) : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )

    class WrongPhoneCode(message: (Context) -> String) : ForYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )
}

fun unknownError(): ForYouAndMeError.Unknown = unknownError(null)

fun unknownError(text: Text?): ForYouAndMeError.Unknown =
    ForYouAndMeError.Unknown(text?.error?.messageDefault)