package com.foryouandme.researchkit.step.beforestart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foryouandme.R
import com.foryouandme.entity.configuration.HEXColor
import com.foryouandme.entity.configuration.HEXGradient
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.evalOnMain
import com.foryouandme.core.ext.html.setHtmlText
import com.foryouandme.core.ext.setOnClickListenerAsync
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_welcome.*


class WelcomeStepFragment : StepFragment(R.layout.step_welcome) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<WelcomeStep>(indexArg())

        startCoroutineAsync {
            setupView()
            step?.let { applyData(it) }
        }
    }

    private suspend fun setupView(): Unit =
        evalOnMain {

            remind_me_later.setOnClickListenerAsync { viewModel.reschedule(taskNavController()) }

            start_now.setOnClickListenerAsync { next() }
            start_now_full.setOnClickListenerAsync { next() }

        }

    private suspend fun applyData(
        step: WelcomeStep
    ): Unit =

        evalOnMain {

            root.setBackgroundColor(step.backgroundColor)

            if (step.image != null) image.setImageResource(step.image)

            title.setHtmlText(step.title(requireContext()), true)
            title.setTextColor(step.titleColor)

            description.setHtmlText(step.description(requireContext()), true)
            description.setTextColor(step.descriptionColor)

            remind_me_later.background = button(step.remindButtonColor)
            remind_me_later.text = step.remindButton(requireContext())
            remind_me_later.setTextColor(step.remindButtonTextColor)
            remind_me_later.isVisible = step.remindMeLater

            start_now.background = button(step.startButtonColor)
            start_now.text = step.startButton(requireContext())
            start_now.setTextColor(step.startButtonTextColor)
            start_now.isVisible = step.remindMeLater

            start_now_full.background = button(step.startButtonColor)
            start_now_full.text = step.startButton(requireContext())
            start_now_full.setTextColor(step.startButtonTextColor)
            start_now_full.isVisible = step.remindMeLater.not()

            shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    HEXColor.parse(step.shadowColor)
                ).drawable(0.3f)

        }

}