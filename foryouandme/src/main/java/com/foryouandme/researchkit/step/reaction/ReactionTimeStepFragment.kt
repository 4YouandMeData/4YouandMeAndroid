package com.foryouandme.researchkit.step.reaction

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.databinding.StepReactionTimeBinding
import com.foryouandme.researchkit.step.StepFragment

class ReactionTimeStepFragment : StepFragment(R.layout.step_reaction_time) {

    private val binding: StepReactionTimeBinding?
        get() = view?.let { StepReactionTimeBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyData()

    }

    fun applyData() {

        val step = taskViewModel.getStepByIndexAs<ReactionTimeStep>(indexArg())
        val viewBinding = binding

        if (step != null && viewBinding != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.title.text = step.titleText ?: getString(R.string.REATION_TIME_title)
            viewBinding.title.setTextColor(step.titleTextColor)

        }

    }

}