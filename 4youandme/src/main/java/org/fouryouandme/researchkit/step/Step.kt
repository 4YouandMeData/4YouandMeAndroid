package org.fouryouandme.researchkit.step

abstract class Step(
    val identifier: String,
    val backImage: Int?,
    val view: () -> StepFragment
)
