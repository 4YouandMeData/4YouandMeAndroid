package com.foryouandme.researchkit.step.introduction

data class IntroductionState(
    val step: IntroductionStep? = null,
    val currentImageIndex: Int = 0
)

sealed class IntroductionAction {

    data class SetStep(val step: IntroductionStep?) : IntroductionAction()

}