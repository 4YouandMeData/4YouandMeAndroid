package com.foryouandme.core.ext

fun <T> catchToNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Throwable) {
        if (e.isCoroutineException()) throw e
        null
    }

fun catch(block: () -> Unit, error: (Throwable) -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        error(e)
    }
}

suspend fun <T> catchToNullSuspend(block: suspend () -> T): T? =
    try {
        block()
    } catch (e: Throwable) {
        if (e.isCoroutineException()) throw e
        null
    }

suspend fun <T> catchSuspend(block: suspend () -> T, error: suspend (Throwable) -> T): T =
    try {
        block()
    } catch (e: Throwable) {
        if (e.isCoroutineException()) throw e
        error(e)
    }
