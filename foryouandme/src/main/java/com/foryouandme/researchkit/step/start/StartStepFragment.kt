package com.foryouandme.researchkit.step.start

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.databinding.StepStartBinding
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.researchkit.step.StepFragment


class StartStepFragment : StepFragment(R.layout.step_start) {

    private val binding: StepStartBinding?
        get() = view?.let { StepStartBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            taskViewModel.getStepByIndexAs<StartStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: StartStep
    ) {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.title.text = step.title(requireContext())
            viewBinding.title.setTextColor(step.titleColor)

            viewBinding.description.text = step.description(requireContext())
            viewBinding.description.setTextColor(step.descriptionColor)

            viewBinding.action1.background = button(step.buttonColor)
            viewBinding.action1.text = step.button(requireContext())
            viewBinding.action1.setTextColor(step.buttonTextColor)
            viewBinding.action1.setOnClickListener { next() }

        }
    }

}