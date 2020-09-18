package org.fouryouandme.core.ext

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

inline fun <T1, T2, A> mapNotNull(one: T1?, two: T2?, block: (T1, T2) -> A): A? =
    if (one != null && two != null) block(one, two)
    else null

