package com.foryouandme.researchkit.step.range

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
class RangeViewModel @Inject constructor() : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(RangeState())
    val stateFlow = state as StateFlow<RangeState>

    /* --- events --- */

    private val event = MutableSharedFlow<UIEvent<RangeEvents>>(replay = 1)
    val eventFlow = event as SharedFlow<UIEvent<RangeEvents>>

    /* --- step --- */

    private suspend fun setStep(step: RangeStep) {
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
            val roundedValue = (value * step.maxValue).roundToInt()
            state.emit(
                state.value.copy(
                    value = roundedValue,
                    valuePercent = value,
                    canGoNext = true
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
            event.emit(RangeEvents.Skip(getResult(), skip.target).toUIEvent())
        else
            event.emit(RangeEvents.Next(getResult()).toUIEvent())

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

    fun execute(action: RangeAction) {
        when (action) {
            is RangeAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is RangeAction.SelectValue ->
                viewModelScope.launchSafe { selectValue(action.value) }
            RangeAction.Next ->
                viewModelScope.launchSafe { checkSkip() }
        }
    }

}