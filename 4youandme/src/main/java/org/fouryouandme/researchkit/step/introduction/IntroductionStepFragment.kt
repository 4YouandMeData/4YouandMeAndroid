package org.fouryouandme.researchkit.step.introduction

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.step_introduction.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment
import kotlin.math.roundToInt


class IntroductionStepFragment : StepFragment(R.layout.step_introduction) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<IntroductionStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: IntroductionStep
    ): Unit {

        root.setBackgroundColor(step.backgroundColor)

        val lp = image.layoutParams
        val displayMetrics = DisplayMetrics()

        (context as FragmentActivity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        lp.height = (height * 0.4).roundToInt()
        image.layoutParams = lp
        image.setImageResource(step.image)
        image.setBackgroundColor(Color.argb(255, 227, 227, 227))

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