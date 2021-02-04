package com.foryouandme.researchkit.step.introduction

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.foryouandme.R
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_introduction.*
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
    ) {

        root.setBackgroundColor(step.backgroundColor)

        val lp = image.layoutParams
        val displayMetrics = DisplayMetrics()

        requireActivity().windowManager
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

        action_1.background = button(step.buttonColor)
        action_1.text = step.button(requireContext())
        action_1.setTextColor(step.buttonTextColor)
        action_1.setOnClickListener { startCoroutineAsync { next() } }

    }

}