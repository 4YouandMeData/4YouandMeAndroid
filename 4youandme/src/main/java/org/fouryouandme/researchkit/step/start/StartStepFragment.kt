package org.fouryouandme.researchkit.step.start

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.step_introduction.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment


class StartStepFragment : StepFragment(R.layout.step_start) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<StartStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: StartStep
    ): Unit {

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title(requireContext())
        title.setTextColor(step.titleColor)

        description.text = step.description(requireContext())
        description.setTextColor(step.descriptionColor)

        next.background = button(step.buttonColor)
        next.text = step.button(requireContext())
        next.setTextColor(step.buttonTextColor)
        next.setOnClickListener { startCoroutineAsync { next() } }

    }

}