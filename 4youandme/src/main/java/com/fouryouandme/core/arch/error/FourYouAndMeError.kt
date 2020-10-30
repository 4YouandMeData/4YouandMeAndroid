package com.fouryouandme.core.arch.error

import android.content.Context
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.Text

sealed class FourYouAndMeError(val title: (Context) -> String, val message: (Context) -> String) {

    /* --- generic --- */

    object MissingConfiguration : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { it.getString(R.string.ERROR_generic) }
    )

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
        val endpoint: String?,
        val method: String?,
        val token: String?,
        val requestBody: String?,
        val responseBody: String?,
        val errorMessage: (Context) -> String = defaultNetworkErrorMessage(null)
    ) : FourYouAndMeError({ it.getString(R.string.ERROR_title) }, errorMessage)

    /* --- auth --- */

    object UserNotLoggedIn : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        { it.getString(R.string.ERROR_generic) }
    )

    class MissingPhoneNumber(message: (Context) -> String) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )

    class WrongPhoneCode(message: (Context) -> String) : FourYouAndMeError(
        { it.getString(R.string.ERROR_title) },
        message
    )
}

fun unknownError(): FourYouAndMeError.Unknown = unknownError(null)

fun unknownError(text: Text?): FourYouAndMeError.Unknown =
    FourYouAndMeError.Unknown(text?.error?.messageDefault)