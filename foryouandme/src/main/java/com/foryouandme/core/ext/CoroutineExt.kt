package com.foryouandme.core.ext

import kotlinx.coroutines.*
import timber.log.Timber

fun <T> CoroutineScope.launchSafe(block: suspend () -> T): Job =
    launch(CoroutineExceptionHandler { _, error ->
        Timber.tag("ERROR").e(error.toString())
    }) {

        block()

    }


data class Action(
    val block: suspend () -> Unit,
    val error: suspend (Throwable) -> Unit
)

fun action(
    block: suspend () -> Unit,
    error: suspend (Throwable) -> Unit
) = Action(block, error)

fun CoroutineScope.launchAction(action: Action): Job =
    launch(CoroutineExceptionHandler { _, _ -> }) {

        catchSuspend({ action.block() }, { action.error(it) })

    }

fun Throwable.isCoroutineException() =
    this is CancellationException || this is TimeoutCancellationException