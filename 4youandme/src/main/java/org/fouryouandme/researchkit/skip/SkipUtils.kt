package org.fouryouandme.researchkit.skip

fun isInOptionalRange(value: Int, min: Int?, max: Int?): Boolean =
    when {

        min == null && max == null -> true
        min != null && max != null -> value >= min && value <= max
        min != null && max == null -> value >= min
        min == null && max != null -> value <= max
        else -> false

    }