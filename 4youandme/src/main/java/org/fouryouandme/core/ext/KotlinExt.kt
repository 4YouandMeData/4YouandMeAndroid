package org.fouryouandme.core.ext

import arrow.core.Tuple2
import arrow.core.toT

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

