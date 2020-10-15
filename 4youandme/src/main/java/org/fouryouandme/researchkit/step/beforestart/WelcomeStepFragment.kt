package org.fouryouandme.researchkit.step.beforestart

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.step_welcome.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.HEXColor
import org.fouryouandme.core.entity.configuration.HEXGradient
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.html.setHtmlText
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment


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

            remind_me_later.setOnClickListener {
                startCoroutineAsync {
                    // TODO: call API to postpone task
                    startCoroutineAsync { viewModel.back(stepNavController(), taskNavController()) }
                }
            }

            start_now.setOnClickListener { startCoroutineAsync { next() } }
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

            start_now.background = button(step.startButtonColor)
            start_now.text = step.startButton(requireContext())
            start_now.setTextColor(step.startButtonTextColor)

            shadow.background =
                HEXGradient.from(
                    HEXColor.transparent(),
                    HEXColor.parse(step.shadowColor)
                ).drawable(0.3f)

        }

}