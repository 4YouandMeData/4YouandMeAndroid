package com.foryouandme.domain.error

import android.content.Context

sealed class ForYouAndMeException : Throwable() {

    /* --- generic --- */

    object MissingConfiguration : ForYouAndMeException()

    object Unknown: ForYouAndMeException()

    /* --- network --- */

    class NetworkErrorUnknownHost: ForYouAndMeException()

    class NetworkErrorTimeOut: ForYouAndMeException()

    class NetworkHTTPException: ForYouAndMeException()

    /* --- auth --- */

    object UserNotLoggedIn : ForYouAndMeException()

    class MissingPhoneNumber() : ForYouAndMeException()

    class WrongPhoneCode(message: (Context) -> String) : ForYouAndMeException()

}