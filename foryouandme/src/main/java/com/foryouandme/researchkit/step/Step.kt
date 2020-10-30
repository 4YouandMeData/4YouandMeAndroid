package com.foryouandme.researchkit.step

abstract class Step(
    val identifier: String,
    val back: Back?,
    val skip: Skip?,
    val view: () -> StepFragment
)

data class Skip(
    val text: String,
    val color: Int
)

data class Back(
    val image: Int
)
