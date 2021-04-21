package com.foryouandme.researchkit.step.holepeg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.entity.task.holepeg.HolePegAttempt
import com.foryouandme.entity.task.holepeg.Peg
import com.foryouandme.entity.task.holepeg.toHolePegAttempts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HolePegViewModel @Inject constructor() : ViewModel() {

    private val state = MutableStateFlow(HolePegState())
    val stateFlow = state as StateFlow<HolePegState>

    private val eventChannel = Channel<HolePegEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    /* --- step --- */

    private suspend fun setStep(step: HolePegStep?) {

        val attempts = step?.subSteps?.toHolePegAttempts() ?: emptyList()

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

        val currentAttempt = state.value.currentAttempt

        if (currentAttempt != null) {

            if (targetReached)
            // the target is reached
                if (currentAttempt.pegs.size < currentAttempt.totalPegs) nextPeg(currentAttempt)
                else nextAttempt(currentAttempt)
            else
            // the target is not reached, increment the error count for this attempt
                attemptError(currentAttempt)

        }

    }


    private suspend fun nextPeg(currentAttempt: HolePegAttempt) {
        /*
          set the end time for the current peg
          and create a new object for the next peg
        */
        val attempt =
            currentAttempt.copy(
                pegs =
                currentAttempt.pegs.mapIndexed { index, holePeg ->
                    if (index == currentAttempt.pegs.lastIndex)
                        holePeg.copy(endDate = ZonedDateTime.now())
                    else
                        holePeg
                }.plus(Peg())
            )

        val attempts = state.value.attempts.map { if (it.id == attempt.id) attempt else it }

        state.emit(
            state.value.copy(
                isDragging = false,
                attempts = attempts,
                currentAttempt = attempt
            )
        )

    }

    private suspend fun nextAttempt(currentAttempt: HolePegAttempt) {

        // select the next attempt if available
        val currentAttemptIndex =
            state.value
                .attempts
                .indexOfFirst { it.id == currentAttempt.id }

        val attempt =
            if (currentAttemptIndex >= 0)
                state.value.attempts.getOrNull(currentAttemptIndex + 1)
            else {
                // no more attempts, end the step
                eventChannel.send(HolePegEvent.Completed)
                return
            }

        val attempts = state.value.attempts.map {
            when (it.id) {
                attempt?.id -> attempt
                currentAttempt.id -> it.copy(endDate = ZonedDateTime.now())
                else -> it
            }
        }

        state.emit(
            state.value.copy(
                isDragging = false,
                attempts = attempts,
                currentAttempt = attempt
            )
        )

    }

    private suspend fun attemptError(currentAttempt: HolePegAttempt) {

        val attempt = currentAttempt.copy(errorCount = currentAttempt.errorCount + 1)

        val attempts = state.value.attempts.map { if (it.id == attempt.id) attempt else it }

        state.emit(
            state.value.copy(
                isDragging = false,
                attempts = attempts,
                currentAttempt = attempt
            )
        )

    }

    private suspend fun addDistance(distance: Float) {

        val currentAttempt = state.value.currentAttempt

        if (currentAttempt != null) {

            val pegs = currentAttempt.pegs.mapIndexed { index, peg ->
                if (index == currentAttempt.pegs.lastIndex)
                    peg.copy(distance = peg.distance + distance)
                else
                    peg
            }
            val attempt = currentAttempt.copy(pegs = pegs)

            val attempts = state.value.attempts.map { if (it.id == attempt.id) attempt else it }

            state.emit(
                state.value.copy(
                    attempts = attempts,
                    currentAttempt = attempt
                )
            )

        }

    }

    /* --- state event --- */

    fun execute(action: HolePegAction) {
        when (action) {
            is HolePegAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is HolePegAction.EndDragging ->
                viewModelScope.launchSafe { endDragging(action.targetReached) }
            HolePegAction.StartDragging ->
                viewModelScope.launchSafe { startDragging() }
            is HolePegAction.OnDrag ->
                viewModelScope.launchSafe { addDistance(action.distance) }
        }
    }

}