package com.foryouandme.core.researchkit.step.page

data class FYAMPageStepState(
    val step: FYAMPageStep? = null
)

sealed class FYAMPageStepAction {
    data class SetStep(val step: FYAMPageStep): FYAMPageStepAction()
}