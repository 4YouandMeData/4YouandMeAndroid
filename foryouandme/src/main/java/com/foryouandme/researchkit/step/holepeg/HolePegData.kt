package com.foryouandme.researchkit.step.holepeg

import com.foryouandme.entity.task.holepeg.HolePegAttempt
import com.foryouandme.entity.task.holepeg.toHolePegAttempts

data class HolePegState(
    val step: HolePegStep? = null,
    val isDragging: Boolean = false,
    val attempts: List<HolePegAttempt> = step?.subSteps?.toHolePegAttempts() ?: emptyList(),
    val currentAttempt: HolePegAttempt? = attempts.firstOrNull()
)

sealed class HolePegSateEvent {

    data class SetStep(val step: HolePegStep?) : HolePegSateEvent()
    object StartDragging : HolePegSateEvent()
    data class OnDrag(val distance: Float): HolePegSateEvent()
    data class EndDragging(val targetReached: Boolean) : HolePegSateEvent()

}





