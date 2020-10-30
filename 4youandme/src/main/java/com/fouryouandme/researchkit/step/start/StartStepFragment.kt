package com.fouryouandme.researchkit.step.start

import android.os.Bundle
import android.view.View
import com.fouryouandme.R
import com.fouryouandme.core.entity.configuration.button.button
import com.fouryouandme.core.ext.startCoroutineAsync
import com.fouryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_introduction.*


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