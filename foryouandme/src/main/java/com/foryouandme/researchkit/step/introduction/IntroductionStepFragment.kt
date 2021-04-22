package com.foryouandme.researchkit.step.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.holepeg.HolePeg
import com.foryouandme.researchkit.step.introduction.compose.Introduction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionStepFragment : StepFragment() {

    private val viewModel: IntroductionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                Introduction { next() }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<IntroductionStep>(indexArg())
        viewModel.execute(IntroductionAction.SetStep(step))

    }

    private fun applyData(
        step: IntroductionStep
    ) {

        /*root.setBackgroundColor(step.backgroundColor)

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
        action_1.setOnClickListener { startCoroutineAsync { next() } }*/

    }

}