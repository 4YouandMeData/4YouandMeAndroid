package com.foryouandme.researchkit.step.introduction

data class IntroductionState(
    val step: IntroductionStep? = null,
    val currentImage: Int? = step?.image
)

sealed class IntroductionAction {

    data class SetStep(val step: IntroductionStep?) : IntroductionAction()

}