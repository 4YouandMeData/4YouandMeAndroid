package org.fouryouandme.core.ext

import arrow.core.Either
import arrow.core.Tuple2
import arrow.core.toT
import org.fouryouandme.core.arch.error.FourYouAndMeError

fun List<Tuple2<Boolean, (suspend () -> Either<FourYouAndMeError, Unit>)?>>.countAndAccumulate(
): Tuple2<Int, MutableList<suspend () -> Either<FourYouAndMeError, Unit>>> =
    fold(
        0 toT mutableListOf(),
        { acc,
          item ->
            item.b?.let { acc.b.add(it) }
            val count = acc.a + if (item.a) 1 else 0
            count toT acc.b
        }
    )