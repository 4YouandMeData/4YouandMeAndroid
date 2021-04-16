package com.foryouandme.researchkit.step.holepeg

import com.foryouandme.entity.task.holepeg.HolePegAttempt
import com.foryouandme.entity.task.holepeg.toHolePegAttempts
import org.threeten.bp.ZonedDateTime

data class HolePegState(
    val step: HolePegStep? = null,
    val isDragging: Boolean = false,
    val attempts: List<HolePegAttempt> = step?.subSteps?.toHolePegAttempts() ?: emptyList(),
    val currentAttempt: HolePegAttempt? = attempts.firstOrNull(),
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class HolePegAction {

    data class SetStep(val step: HolePegStep?) : HolePegAction()
    object StartDragging : HolePegAction()
    data class OnDrag(val distance: Float): HolePegAction()
    data class EndDragging(val targetReached: Boolean) : HolePegAction()

}

sealed class HolePegEvent {

    object Completed: HolePegEvent()

}





