package com.foryouandme.researchkit.step.number

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.databinding.StepNumberRangePickerBinding
import com.foryouandme.entity.configuration.background.shadow
import com.foryouandme.researchkit.result.SingleAnswerResult
import com.foryouandme.researchkit.skip.isInOptionalRange
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.utils.applyImage
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZonedDateTime

@AndroidEntryPoint
class NumberRangePickerStepFragment : StepFragment(R.layout.step_number_range_picker) {

    private val binding: StepNumberRangePickerBinding?
        get() = view?.let { StepNumberRangePickerBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel.getStepByIndexAs<NumberRangePickerStep>(indexArg())?.let { applyData(it) }
    }

    private fun applyData(step: NumberRangePickerStep) {

        val viewBinding = binding

        val start = ZonedDateTime.now()

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        step.image?.let { viewBinding?.icon?.applyImage(it) }
        viewBinding?.icon?.isVisible = step.image != null

        viewBinding?.question?.text = step.question(requireContext())
        viewBinding?.question?.setTextColor(step.questionColor)

        val values = getValues(step)

        viewBinding?.numberPicker?.minValue = 0
        viewBinding?.numberPicker?.maxValue = values.size - 1
        viewBinding?.numberPicker?.displayedValues = values.toTypedArray()

        viewBinding?.shadow?.background = shadow(step.shadowColor)

        viewBinding?.button?.applyImage(step.buttonImage)
        viewBinding?.button?.setOnClickListener {

            val answer =
                binding
                    ?.numberPicker
                    ?.value
                    ?.let { binding?.numberPicker?.displayedValues?.get(it) }

            answer?.let {

                addResult(

                    SingleAnswerResult(
                        step.identifier,
                        start,
                        ZonedDateTime.now(),
                        step.questionId,
                        answer
                    )

                )

            }

            checkSkip(step)

        }

    }

    private fun getValues(step: NumberRangePickerStep): List<String> {

        val list = mutableListOf<String>()

        step.minDisplayValue?.let { list.add(it) }

        for (i in step.min..step.max) {
            list.add(i.toString())
        }

        step.maxDisplayValue?.let { list.add(it) }

        return list

    }

    private fun checkSkip(step: NumberRangePickerStep) {

        val viewBinding = binding

        val skip = step.skips.firstOrNull()

        val index = viewBinding?.numberPicker?.value
        val value = index?.let { viewBinding.numberPicker.displayedValues[it] }
        val numericValue = value?.let { getNumericalValue(it, step) }

        if (
            skip != null &&
            numericValue != null &&
            isInOptionalRange(numericValue, skip.min, skip.max)
        )
            skipTo(skip.stepId)
        else
            next()

    }

    private fun getNumericalValue(value: String, step: NumberRangePickerStep): Int =
        when (value) {
            step.minDisplayValue -> step.min - 1
            step.maxDisplayValue -> step.max + 1
            else -> value.toInt()
        }

}