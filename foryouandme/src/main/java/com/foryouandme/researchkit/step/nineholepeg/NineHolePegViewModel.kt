package com.foryouandme.researchkit.step.nineholepeg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.entity.task.nineholepeg.toNineHolePegAttempts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NineHolePegViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow(NineHolePegState())
    val stateFlow = state as StateFlow<NineHolePegState>

    /* --- step --- */

    private suspend fun setStep(step: NineHolePegStep?) {

        val attempts = step?.subSteps?.toNineHolePegAttempts() ?: emptyList()

        state.emit(
            state.value.copy(
                step = step,
                attempts = attempts,
                currentAttempt = attempts.firstOrNull()
            )
        )
    }

    /* --- dragging --- */

    private suspend fun startDragging() {
        state.emit(state.value.copy(isDragging = true))
    }

    private suspend fun endDragging(targetReached: Boolean) {

        val attempt =
            if (targetReached) {
                // the target is reached, select the next attempt if available
                val currentAttemptIndex =
                    state.value
                        .attempts
                        .indexOfFirst { it.id == state.value.currentAttempt?.id }

                if (currentAttemptIndex >= 0)
                    state.value.attempts.getOrNull(currentAttemptIndex + 1)
                else
                    null

            } else
            // the target is not reached, increment the error count for this attempt
                state.value.currentAttempt?.copy(
                    errorCount = (state.value.currentAttempt?.errorCount ?: 0) + 1
                )

        val attempts = state.value.attempts.map { if (it.id == attempt?.id) attempt else it }

        state.emit(
            state.value.copy(
                isDragging = false,
                attempts = attempts,
                currentAttempt = attempt
            )
        )

    }

    /* --- state event --- */

    fun execute(stateEvent: NineHolePegSateEvent) {
        when (stateEvent) {
            is NineHolePegSateEvent.SetStep ->
                viewModelScope.launchSafe { setStep(stateEvent.step) }
            is NineHolePegSateEvent.EndDragging ->
                viewModelScope.launchSafe { endDragging(stateEvent.targetReached) }
            NineHolePegSateEvent.StartDragging ->
                viewModelScope.launchSafe { startDragging() }
        }
    }

}