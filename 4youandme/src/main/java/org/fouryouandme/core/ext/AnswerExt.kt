package org.fouryouandme.core.ext

import arrow.Kind
import arrow.core.Either
import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.toT
import arrow.fx.ForIO
import org.fouryouandme.core.arch.error.FourYouAndMeError

fun <F> List<Tuple2<Boolean, Option<Kind<F, Either<FourYouAndMeError, Unit>>>>>.countAndAccumulate() =
    fold(
        0 toT mutableListOf<Kind<F, Either<FourYouAndMeError, Unit>>>(),
        { acc, item ->
            item.b.map { acc.b.add(it) }
            val count = acc.a + if (item.a) 1 else 0
            count toT acc.b
        }
    )