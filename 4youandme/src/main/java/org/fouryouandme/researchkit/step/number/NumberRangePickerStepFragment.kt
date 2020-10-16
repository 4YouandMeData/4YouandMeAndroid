package org.fouryouandme.researchkit.step.number

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_number_range_picker.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.skip.isInOptionalRange
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.threeten.bp.ZonedDateTime

class NumberRangePickerStepFragment : StepFragment(R.layout.step_number_range_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            val step =
                viewModel.getStepByIndexAs<NumberRangePickerStep>(indexArg())

            step?.let { applyData(it) }
        }
    }

    private suspend fun applyData(
        step: NumberRangePickerStep
    ): Unit =

        evalOnMain {
            val start = ZonedDateTime.now()

            root.setBackgroundColor(step.backgroundColor)

            step.image?.let { icon.applyImage(it) }
            icon.isVisible = step.image != null

            question.text = step.question(requireContext())
            question.setTextColor(step.questionColor)

            val values = getValues(step)

            number_picker.minValue = 0
            number_picker.maxValue = values.size - 1
            number_picker.displayedValues = values.toTypedArray()

            shadow.background = shadow(step.shadowColor)

            button.applyImage(step.buttonImage)
            button.setOnClickListener {
                startCoroutineAsync {

                    viewModel.addResult(

                        SingleAnswerResult(
                            step.identifier,
                            start,
                            ZonedDateTime.now(),
                            step.questionId,
                            number_picker.displayedValues[number_picker.value]
                        )

                    )

                    checkSkip(step)
                }
            }

        }

    private fun getValues(step: NumberRangePickerStep): List<String> {

        val list = mutableListOf<String>()

        step.minDisplayValue?.let { list.add(it) }

        for (i in step.min..step.max) list.add(i.toString())

        step.maxDisplayValue?.let { list.add(it) }

        return list

    }

    private suspend fun checkSkip(step: NumberRangePickerStep): Unit =
        evalOnMain {

            val skip = step.skips.firstOrNull()

            val index = number_picker.value
            val value = number_picker.displayedValues[index]
            val numericValue = getNumericalValue(value, step)

            if (skip != null && isInOptionalRange(numericValue, skip.min, skip.max))
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