package com.foryouandme.researchkit.step.nineholepeg

import com.foryouandme.entity.task.nineholepeg.NineHolePegAttempt
import com.foryouandme.entity.task.nineholepeg.toNineHolePegAttempts

data class NineHolePegState(
    val step: NineHolePegStep? = null,
    val isDragging: Boolean = false,
    val attempts: List<NineHolePegAttempt> = step?.subSteps?.toNineHolePegAttempts() ?: emptyList(),
    val currentAttempt: NineHolePegAttempt? = attempts.firstOrNull()
)

sealed class NineHolePegSateEvent {

    data class SetStep(val step: NineHolePegStep?) : NineHolePegSateEvent()
    object StartDragging : NineHolePegSateEvent()
    data class EndDragging(val targetReached: Boolean) : NineHolePegSateEvent()

}





