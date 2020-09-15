package org.fouryouandme.researchkit.step.start

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.step_introduction.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment


class StartStepFragment : StepFragment(R.layout.step_start) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.StartStep>(indexArg())

        step.map { applyData(it) }
    }

    private fun applyData(
        step: Step.StartStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.text = step.description
        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        next.background = button(step.configuration.theme.primaryColorEnd.color())
        next.text = step.button
        next.setTextColor(step.configuration.theme.secondaryColor.color())
        next.setOnClickListener { next(step.close) }

    }

}