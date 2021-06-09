package com.foryouandme.researchkit.step.beforestart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.databinding.StepWelcomeBinding
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.researchkit.step.StepFragment


class WelcomeStepFragment : StepFragment(R.layout.step_welcome) {

    val binding: StepWelcomeBinding?
        get() = view?.let { StepWelcomeBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            taskViewModel.getStepByIndexAs<WelcomeStep>(indexArg())

        setupView()
        step?.let { applyData(it) }

    }

    private fun setupView() {

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.remindMeLater.setOnClickListener { reschedule() }

            viewBinding.startNow.setOnClickListener { next() }
            viewBinding.startNowFull.setOnClickListener { next() }
        }

    }

    private fun applyData(step: WelcomeStep) {

        val viewBinding = binding

        if (viewBinding != null) {
            viewBinding.root.setBackgroundColor(step.backgroundColor)

            if (step.image != null) viewBinding.image.setImageResource(step.image)

            viewBinding.title.setHtmlText(step.title(requireContext()), true)
            viewBinding.title.setTextColor(step.titleColor)

            viewBinding.description.setHtmlText(step.description(requireContext()), true)
            viewBinding.description.setTextColor(step.descriptionColor)

            viewBinding.remindMeLater.background = button(step.remindButtonColor)
            viewBinding.remindMeLater.text = step.remindButton(requireContext())
            viewBinding.remindMeLater.setTextColor(step.remindButtonTextColor)
            viewBinding.remindMeLater.isVisible = step.remindMeLater

            viewBinding.startNow.background = button(step.startButtonColor)
            viewBinding.startNow.text = step.startButton(requireContext())
            viewBinding.startNow.setTextColor(step.startButtonTextColor)
            viewBinding.startNow.isVisible = step.remindMeLater

            viewBinding.startNowFull.background = button(step.startButtonColor)
            viewBinding.startNowFull.text = step.startButton(requireContext())
            viewBinding.startNowFull.setTextColor(step.startButtonTextColor)
            viewBinding.startNowFull.isVisible = step.remindMeLater.not()

            viewBinding.shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    HEXColor.parse(step.shadowColor)
                ).drawable(0.3f)

        }
    }

}