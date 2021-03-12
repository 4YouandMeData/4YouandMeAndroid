package com.foryouandme.researchkit.step.reaction

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.databinding.StepReactionTimeBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ReactionTimeStepFragment : StepFragment(R.layout.step_reaction_time) {

    private val binding: StepReactionTimeBinding?
        get() = view?.let { StepReactionTimeBinding.bind(it) }

    private val viewModel: ReactionTimeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    ReactionTimeStateUpdate.Spawn -> spawnCircle()
                }
            }
            .observeIn(this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyData()

    }

    private fun applyData() {

        val step = taskViewModel.getStepByIndexAs<ReactionTimeStep>(indexArg())
        val viewBinding = binding

        if (step != null && viewBinding != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.title.text = step.titleText ?: getString(R.string.REATION_TIME_title)
            viewBinding.title.setTextColor(step.titleTextColor)

            viewBinding.attempt.setTextColor(step.attemptsTextColor)
            applyAttemptText()

            viewBinding.reaction.isVisible = false
            viewBinding.reaction.addValueCallback(
                KeyPath("Shape Outlines", "**"),
                LottieProperty.COLOR_FILTER,
                {
                    PorterDuffColorFilter(
                        step.checkMarkBackgroundColor,
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            )

            viewBinding.reaction.addValueCallback(
                KeyPath("Line Outlines", "**"),
                LottieProperty.COLOR_FILTER,
                {
                    PorterDuffColorFilter(
                        step.checkMarkColor,
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
            )

            viewModel.execute(
                ReactionTimeStateEvent.StartAttempt(
                    step.maximumStimulusIntervalSeconds,
                    step.minimumStimulusIntervalSeconds,
                    step.thresholdAcceleration,
                    step.numberOfAttempts,
                    step.timeoutSeconds
                )
            )

        }

    }

    private fun applyAttemptText() {

        val step = taskViewModel.getStepByIndexAs<ReactionTimeStep>(indexArg())
        val viewBinding = binding

        if (step != null && viewBinding != null) {

            viewBinding.attempt.text =
                getString(
                    R.string.REATION_TIME_attempts,
                    viewModel.state.attempt,
                    step.numberOfAttempts
                )

        }

    }

    private fun spawnCircle() {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.reaction.isVisible = true
            viewBinding.reaction.playAnimation()

        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.execute(ReactionTimeStateEvent.StartShakeTracking)
    }

    override fun onPause() {
        super.onPause()
        viewModel.execute(ReactionTimeStateEvent.StopShakeTracking)
    }

}