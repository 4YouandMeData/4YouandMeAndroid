package org.fouryouandme.researchkit.step.picker

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.step_picker.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.result.SingleAnswerResult
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.utils.applyImage
import org.threeten.bp.ZonedDateTime

class PickerStepFragment : StepFragment(R.layout.step_picker) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<PickerStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: PickerStep
    ): Unit {

        val start = ZonedDateTime.now()

        root.setBackgroundColor(step.backgroundColor)

        step.image?.let { icon.applyImage(it) }
        icon.isVisible = step.image != null

        question.text = step.question(requireContext())
        question.setTextColor(step.questionColor)

        number_picker.minValue = 0
        number_picker.maxValue = step.values.size - 1
        number_picker.displayedValues = step.values.toTypedArray()

        shadow.background = shadow(step.shadowColor)

        button.applyImage(step.buttonImage)
        button.setOnClickListener {
            startCoroutineAsync {
                viewModel.addResult(SingleAnswerResult(step.identifier, start, ZonedDateTime.now(), step.questionId, number_picker.displayedValues[number_picker.value]))
                next()
            }
        }

    }

}