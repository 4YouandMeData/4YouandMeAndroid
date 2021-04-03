package com.foryouandme.core.ext

import kotlinx.coroutines.Deferred

fun List<Pair<Boolean, Deferred<Unit>?>>.countAndAccumulate(
): Pair<Int, MutableList<Deferred<Unit>>> =
    fold(
        0 to mutableListOf(),
        { acc,
          item ->
            item.second?.let { acc.second.add(it) }
            val count = acc.first + if (item.first) 1 else 0
            count to acc.second
        }
    )