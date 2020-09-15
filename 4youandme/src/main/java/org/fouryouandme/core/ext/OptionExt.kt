package org.fouryouandme.core.ext

import arrow.Kind
import arrow.core.Option
import arrow.core.getOrElse
import arrow.core.toOption
import arrow.fx.IO
import arrow.fx.typeclasses.ConcurrentFx

fun <T, F> Option<T>.toKind(
    fx: ConcurrentFx<F>,
    none: () -> Kind<F, Option<T>>
): Kind<F, Option<T>> =
    fx.concurrent {

        !fold(
            { none() },
            { just(it.toOption()) }
        )
    }

fun Option<String>.getOrEmpty(): String = getOrElse { "" }

fun Option<Boolean>.getOrFalse(): Boolean = getOrElse { false }

fun Option<IO<Unit>>.orJustUnit(): IO<Unit> = getOrElse { IO.just(Unit) }

