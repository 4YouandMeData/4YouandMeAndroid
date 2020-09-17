package org.fouryouandme.core.ext

import arrow.fx.coroutines.CancellableContinuation
import arrow.fx.coroutines.Disposable
import arrow.fx.coroutines.evalOn
import arrow.fx.coroutines.startCoroutineCancellable
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.Continuation
import kotlin.coroutines.startCoroutine

fun <T> startCoroutine(block: suspend () -> T): Unit =
    suspend { block() }.startCoroutine(Continuation(Dispatchers.Main) { })

fun <T> startCoroutineAsync(block: suspend () -> T): Unit =
    suspend { block() }.startCoroutine(Continuation(Dispatchers.IO) { })

fun <T> startCoroutineCancellableAsync(block: suspend () -> T): Disposable =
    suspend { block() }.startCoroutineCancellable(CancellableContinuation(Dispatchers.IO) { })

suspend fun <T> evalOnMain(block: suspend () -> T): T =
    evalOn(Dispatchers.Main, block)

suspend fun <T> evalOnIO(block: suspend () -> T): T =
    evalOn(Dispatchers.IO, block)