package org.fouryouandme.researchkit.step.picker

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.step_number.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.background.shadow
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment

class PickerStepFragment : StepFragment(R.layout.step_number) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<PickerStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: PickerStep
    ): Unit {

        root.setBackgroundColor(step.backgroundColor)

        question.text = step.question(requireContext())
        question.setTextColor(step.questionColor)

        number_picker.minValue = 0
        number_picker.maxValue = step.values.size - 1
        number_picker.displayedValues = step.values.toTypedArray()

        shadow.background = shadow(step.shadowColor)

        button.setImageResource(step.buttonImage)
        button.setOnClickListener {
            startCoroutineAsync { next() }
        }

    }

}