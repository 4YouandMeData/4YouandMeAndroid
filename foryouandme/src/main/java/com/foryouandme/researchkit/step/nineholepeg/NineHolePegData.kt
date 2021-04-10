package com.foryouandme.researchkit.step.nineholepeg

data class NineHolePegState(
    val step: NineHolePegStep? = null,
    val isDragging: Boolean = false
)

sealed class NineHolePegSateEvent {

    data class SetStep(val step: NineHolePegStep?) : NineHolePegSateEvent()
    data class SetDragging(val isDragging: Boolean) : NineHolePegSateEvent()

}