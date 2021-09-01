package com.foryouandme.researchkit.step.scale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.result.SingleIntAnswerResult
import com.foryouandme.researchkit.skip.isInOptionalRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ScaleViewModel @Inject constructor() : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(ScaleState())
    val stateFlow = state as StateFlow<ScaleState>

    /* --- events --- */

    private val event = MutableSharedFlow<UIEvent<ScaleEvents>>(replay = 1)
    val eventFlow = event as SharedFlow<UIEvent<ScaleEvents>>

    /* --- step --- */

    private suspend fun setStep(step: ScaleStep) {

        state.emit(
            state.value.copy(
                step = step,
                value = step.minValue,
                valuePercent = 0f
            )
        )
    }

    /* --- value --- */

    private suspend fun selectValue(value: Float) {

        val step = state.value.step
        if (step != null) {
            val roundedValue =
                (value * (step.maxValue - step.minValue)) + step.minValue
            val roundedValueToInterval =
                ((roundedValue - step.minValue)/step.interval.toFloat()).roundToInt() * step.interval + step.minValue
            state.emit(
                state.value.copy(
                    value = roundedValueToInterval,
                    valuePercent = value,
                    canGoNext = true
                )
            )
        }
    }

    private suspend fun snapValue() {

        val step = state.value.step
        if (step != null) {
            val snapValue =
                (state.value.value - step.minValue).toFloat() /
                        (step.maxValue - step.minValue).toFloat()
            state.emit(
                state.value.copy(
                    valuePercent = snapValue,
                )
            )
        }
    }

    /* --- skip --- */

    private suspend fun checkSkip() {

        val skip = state.value.step?.skips?.firstOrNull()
        val value = state.value.value

        if (
            skip != null &&
            isInOptionalRange(value, skip.min, skip.max)
        )
            event.emit(ScaleEvents.Skip(getResult(), skip.target).toUIEvent())
        else
            event.emit(ScaleEvents.Next(getResult()).toUIEvent())

    }

    /* --- result --- */

    private fun getResult(): SingleIntAnswerResult? {

        val step = state.value.step
        val answer = state.value.value

        return if (step != null)
            SingleIntAnswerResult(
                step.identifier,
                state.value.start,
                ZonedDateTime.now(),
                step.questionId,
                answer
            )
        else null

    }

    /* --- action --- */

    fun execute(action: ScaleAction) {
        when (action) {
            is ScaleAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is ScaleAction.SelectValue ->
                viewModelScope.launchSafe { selectValue(action.value) }
            ScaleAction.Next ->
                viewModelScope.launchSafe { checkSkip() }
            ScaleAction.EndValueSelection ->
                viewModelScope.launchSafe { snapValue() }
        }
    }

}