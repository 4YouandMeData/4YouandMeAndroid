package com.foryouandme.researchkit.step.number

data class NumberPickerState(
    val step: NumberRangePickerStep? = null,
    val values: List<String> = emptyList(),
    val selectedIndex: Int = 0
)

sealed class NumberPickerAction {

    data class SetStep(val step: NumberRangePickerStep) : NumberPickerAction()
    data class SelectValue(val valueIndex: Int) : NumberPickerAction()

}