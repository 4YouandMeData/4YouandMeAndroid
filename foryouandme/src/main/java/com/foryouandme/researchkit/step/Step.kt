package com.foryouandme.researchkit.step

abstract class Step(
    val identifier: String,
    val block: Block? = null,
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

data class Block(
    val identifier: String,
    val color: Int
)
