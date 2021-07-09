package com.foryouandme.researchkit.step.date

import com.foryouandme.researchkit.result.SingleStringAnswerResult
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

data class DatePickerState(
    val step: DatePickerStep? = null,
    val value: LocalDate? = null,
    val canGoNext: Boolean = false,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class DatePickerAction {

    data class SetStep(val step: DatePickerStep) : DatePickerAction()
    data class SetDate(val date: LocalDate) : DatePickerAction()
    object Next : DatePickerAction()

}

sealed class DatePickerEvent {

    data class Next(val result: SingleStringAnswerResult?) : DatePickerEvent()

}

