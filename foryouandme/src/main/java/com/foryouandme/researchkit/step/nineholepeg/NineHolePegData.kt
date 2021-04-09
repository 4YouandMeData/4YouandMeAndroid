package com.foryouandme.researchkit.step.nineholepeg

data class NineHolePegState(
    val step: NineHolePegStep? = null,
)

sealed class NineHolePegSateEvent {

    data class SetStep(val step: NineHolePegStep?) : NineHolePegSateEvent()

}