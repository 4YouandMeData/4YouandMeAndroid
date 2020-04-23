package org.fouryouandme.core.arch.error

data class ErrorPayload<A>(
        val cause: A,
        val error: FourYouAndMeError
)