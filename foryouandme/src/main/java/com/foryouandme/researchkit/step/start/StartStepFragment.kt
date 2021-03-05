package com.foryouandme.researchkit.step.start

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_introduction.*


class StartStepFragment : StepFragment(R.layout.step_start) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            taskViewModel.getStepByIndexAs<StartStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: StartStep
    ) {

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title(requireContext())
        title.setTextColor(step.titleColor)

        description.text = step.description(requireContext())
        description.setTextColor(step.descriptionColor)

        action_1.background = button(step.buttonColor)
        action_1.text = step.button(requireContext())
        action_1.setTextColor(step.buttonTextColor)
        action_1.setOnClickListener { startCoroutineAsync { next() } }

    }

}