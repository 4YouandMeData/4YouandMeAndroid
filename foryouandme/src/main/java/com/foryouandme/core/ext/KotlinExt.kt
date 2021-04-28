package com.foryouandme.core.ext

import arrow.core.*
import kotlinx.coroutines.CancellationException

fun <T> T?.getOr(block: () -> T): T =
    this ?: block()

fun <T, A> T?.fold(nullAction: () -> A, validAction: (T) -> A): A =
    if (this == null)
        nullAction()
    else
        validAction(this)

suspend fun <T, A> T?.foldSuspend(nullAction: suspend () -> A, validAction: suspend (T) -> A): A =
    if (this == null)
        nullAction()
    else
        validAction(this)

fun <T> T?.mapNull(block: () -> T): T = this ?: block()

fun <T1, T2> mapNotNull(one: T1?, two: T2?): Tuple2<T1, T2>? =
    if (one != null && two != null) one toT two
    else null

fun <T1, T2, T3> mapNotNull(one: T1?, two: T2?, three: T3?): Tuple3<T1, T2, T3>? =
    if (one != null && two != null && three != null) Tuple3(one, two, three)
    else null

fun <T> T?.toEither(): Either<Unit, T> =
    this?.right() ?: Unit.left()

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

suspend fun catchSuspend(block: suspend () -> Unit, error: suspend (Throwable) -> Unit) =
    try {
        block()
    } catch (e: Throwable) {
        if (e.isCoroutineException()) throw e
        error(e)
    }
