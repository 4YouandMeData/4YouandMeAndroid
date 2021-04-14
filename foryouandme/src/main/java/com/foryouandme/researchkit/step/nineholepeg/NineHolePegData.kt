package com.foryouandme.researchkit.step.nineholepeg

data class NineHolePegState(
    val step: NineHolePegStep? = null,
    val isDragging: Boolean = false,
)

sealed class NineHolePegSateEvent {

    data class SetStep(val step: NineHolePegStep?) : NineHolePegSateEvent()
    data class SetDragging(val isDragging: Boolean) : NineHolePegSateEvent()

}

sealed class NineHolePegPointPosition {

    object Center: NineHolePegPointPosition()
    object Start: NineHolePegPointPosition()
    object End: NineHolePegPointPosition()

}

sealed class NineHolePegTargetPosition {

    object Start: NineHolePegTargetPosition()
    object StartCenter: NineHolePegTargetPosition()
    object End: NineHolePegTargetPosition()
    object EndCenter: NineHolePegTargetPosition()

}