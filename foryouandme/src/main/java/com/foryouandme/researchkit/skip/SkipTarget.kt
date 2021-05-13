package com.foryouandme.researchkit.skip

sealed class SkipTarget {

    object End: SkipTarget()
    data class StepId(val id: String): SkipTarget()

}