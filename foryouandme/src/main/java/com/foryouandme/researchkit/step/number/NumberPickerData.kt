package com.foryouandme.researchkit.step.number

import com.foryouandme.researchkit.result.SingleStringAnswerResult
import com.foryouandme.researchkit.skip.SkipTarget
import org.threeten.bp.ZonedDateTime

data class NumberPickerState(
    val step: NumberRangePickerStep? = null,
    val values: List<String> = emptyList(),
    val selectedIndex: Int = 0,
    val start: ZonedDateTime = ZonedDateTime.now()
)

sealed class NumberPickerAction {

    data class SetStep(val step: NumberRangePickerStep) : NumberPickerAction()
    data class SelectValue(val valueIndex: Int) : NumberPickerAction()
    object Next : NumberPickerAction()

}

sealed class NumberPickerEvents {

    data class Skip(
        val result: SingleStringAnswerResult?,
        val target: SkipTarget
    ) : NumberPickerEvents()

    data class Next(val result: SingleStringAnswerResult?) : NumberPickerEvents()

}