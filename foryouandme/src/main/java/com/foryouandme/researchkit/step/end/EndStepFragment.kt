package com.foryouandme.researchkit.step.end

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.foryouandme.R
import com.foryouandme.databinding.StepEndBinding
import com.foryouandme.entity.configuration.button.button
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EndStepFragment : StepFragment(R.layout.step_end) {

    private val binding: StepEndBinding?
        get() = view?.let { StepEndBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStepByIndexAs<EndStep>(indexArg())?.let { applyData(it) }

    }

    private fun applyData(step: EndStep) {

        val viewBinding = binding

        viewBinding?.root?.setBackgroundColor(step.backgroundColor)

        viewBinding?.title?.text = step.title(requireContext())
        viewBinding?.title?.setTextColor(step.titleColor)

        viewBinding?.description?.text = step.description(requireContext())
        viewBinding?.description?.setTextColor(step.descriptionColor)

        viewBinding?.action1?.background = button(step.buttonColor)
        viewBinding?.action1?.text = step.button(requireContext())
        viewBinding?.action1?.setTextColor(step.buttonTextColor)
        viewBinding?.action1?.setOnClickListener { end() }

        viewBinding?.animationView?.addValueCallback(
            KeyPath("Shape Outlines", "**"),
            LottieProperty.COLOR_FILTER,
            {
                PorterDuffColorFilter(
                    step.checkMarkBackgroundColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        )

        binding?.animationView?.addValueCallback(
            KeyPath("Line Outlines", "**"),
            LottieProperty.COLOR_FILTER,
            {
                PorterDuffColorFilter(
                    step.checkMarkColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        )
    }

}