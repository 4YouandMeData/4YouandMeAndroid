package org.fouryouandme.core.ext

fun <T, A> T?.foldValue(nullAction: () -> A, validAction: (T) -> A) =
    if (this == null)
        nullAction()
    else
        validAction(this)
