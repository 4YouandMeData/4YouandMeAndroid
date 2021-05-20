package com.foryouandme.researchkit.step.number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.skip.isInOptionalRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NumberRangePickerViewModel @Inject constructor(): ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(NumberPickerState())
    val stateFlow = state as StateFlow<NumberPickerState>

    /* --- step --- */

    private suspend fun setStep(step: NumberRangePickerStep) {
        state.emit(state.value.copy(step = step, values = getValues(step), selectedIndex = 0))
    }

    /* --- values --- */

    private fun getValues(step: NumberRangePickerStep): List<String> {

        val list = mutableListOf<String>()

        step.minDisplayValue?.let { list.add(it) }

        for (i in step.min..step.max) {
            list.add(i.toString())
        }

        step.maxDisplayValue?.let { list.add(it) }

        return list

    }

    private fun getNumericalValue(): Int? {

        val step = state.value.step
        val value = state.value.values.getOrNull(state.value.selectedIndex)

        return if(step != null && value != null) {
            when (value) {
                step.minDisplayValue -> step.min - 1
                step.maxDisplayValue -> step.max + 1
                else -> value.toInt()
            }
        } else null

    }

    private suspend fun selectValue(value: Int) {
        state.emit(state.value.copy(selectedIndex = value))
    }

    /* --- skip --- */

    private fun checkSkip(step: NumberRangePickerStep) {

        val skip = step.skips.firstOrNull()

        val numericValue = getNumericalValue()

        /*if (
            skip != null &&
            numericValue != null &&
            isInOptionalRange(numericValue, skip.min, skip.max)
        )
            //skipTo(skip.target)
        else
            //next()*/

    }

    /* --- action --- */

    fun execute(action: NumberPickerAction){
        when(action) {
            is NumberPickerAction.SetStep ->
                viewModelScope.launchSafe { setStep(action.step) }
            is NumberPickerAction.SelectValue ->
                viewModelScope.launchSafe { selectValue(action.valueIndex) }
        }
    }

}