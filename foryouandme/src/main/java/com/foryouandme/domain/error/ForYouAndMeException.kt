package com.foryouandme.domain.error

sealed class ForYouAndMeException : Throwable() {

    /* --- generic --- */

    object MissingConfiguration : ForYouAndMeException()

    object Unknown: ForYouAndMeException()

    /* --- network --- */

    class NetworkErrorUnknownHost: ForYouAndMeException()

    class NetworkErrorTimeOut: ForYouAndMeException()

    class NetworkHTTPException(val code: Int): ForYouAndMeException()

    /* --- auth --- */

    object UserNotLoggedIn : ForYouAndMeException()

    class MissingPhoneNumber : ForYouAndMeException()

    class WrongCode : ForYouAndMeException()

}