package com.foryouandme.core.arch.error

data class ErrorPayload<A>(
    val cause: A,
    val error: ForYouAndMeError
)