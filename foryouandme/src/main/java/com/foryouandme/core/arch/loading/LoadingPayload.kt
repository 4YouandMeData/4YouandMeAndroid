package com.foryouandme.core.arch.loading

data class LoadingPayload<A>(
    val task: A,
    val active: Boolean
)