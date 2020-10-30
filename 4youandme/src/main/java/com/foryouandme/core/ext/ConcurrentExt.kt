package com.foryouandme.core.ext

import arrow.fx.typeclasses.ConcurrentSyntax
import kotlinx.coroutines.Dispatchers

suspend fun <F> ConcurrentSyntax<F>.onMainThread(action: () -> Unit) {

    continueOn(Dispatchers.Main)
    action()
    continueOn(Dispatchers.IO)

}