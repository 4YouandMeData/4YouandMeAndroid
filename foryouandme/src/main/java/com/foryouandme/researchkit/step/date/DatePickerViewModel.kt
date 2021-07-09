package com.foryouandme.researchkit.step.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.flow.UIEvent
import com.foryouandme.core.arch.flow.toUIEvent
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.result.SingleStringAnswerResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DatePickerViewModel @Inject constructor(
) : ViewModel() {

    /* --- state -- */

    private val state = MutableStateFlow(DatePickerState())
    val stateFlow = state as StateFlow<DatePickerState>

    /* --- events --- */

    private val events = MutableSharedFlow<UIEvent<DatePickerEvent>>(replay = 1)
    val eventsFlow = events as SharedFlow<UIEvent<DatePickerEvent>>

    /* --- step --- */

    private suspend fun setStep(step: DatePickerStep) {

        state.emit(state.value.copy(step = step))

    }

    /* --- date --- */

    private suspend fun setDate(date: LocalDate?) {

        state.emit(
            state.value.copy(
                value = date,
                canGoNext = date != null
            )
        )

    }

    /* --- next --- */

    private suspend fun next() {

        events.emit(DatePickerEvent.Next(getResult()).toUIEvent())

    }

    /* --- result --- */

    private fun getResult(): SingleStringAnswerResult? {

        val step = state.value.step
        val date = getFormattedSelectedDate()

        return if (step != null && date != null)
            SingleStringAnswerResult(
                step.identifier,
                state.value.start,
                ZonedDateTime.now(),
                step.questionId,
                date
            )
        else null

    }

    private fun getFormattedSelectedDate(): String? =
        state.value.value?.format(DateTimeFormatter.ISO_LOCAL_DATE)

    /* --- action --- */

    fun execute(action: DatePickerAction) {

        when (action) {
            is DatePickerAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is DatePickerAction.SetDate ->
                viewModelScope.launchSafe { setDate(action.date) }
            DatePickerAction.Next ->
                viewModelScope.launchSafe { next() }
        }

    }

}