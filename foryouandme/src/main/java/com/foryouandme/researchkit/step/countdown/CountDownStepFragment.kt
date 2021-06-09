package com.foryouandme.researchkit.step.countdown

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.databinding.StepCountdownBinding
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.coroutines.delay

class CountDownStepFragment : StepFragment(R.layout.step_countdown) {

    private val binding: StepCountdownBinding?
        get() = view?.let { StepCountdownBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            taskViewModel.getStepByIndexAs<CountDownStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: CountDownStep
    ) {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            viewBinding.title.text = step.title(requireContext())
            viewBinding.title.setTextColor(step.titleColor)

            viewBinding.description.text = step.description(requireContext())
            viewBinding.description.setTextColor(step.descriptionColor)

            viewBinding.counter.setTextColor(step.counterColor)

            viewBinding.progress.progressTintList =
                ColorStateList.valueOf(step.counterProgressColor)

            startCounter(step.seconds) { next() }
        }
    }

    private fun startCounterAnimation(seconds: Int) {

        val animator = ValueAnimator.ofInt(seconds, 0)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { binding?.counter?.text = (it.animatedValue as Int).toString() }
        animator.interpolator = LinearInterpolator()

        lifecycle.addObserver(LifecycleValueAnimator(animator))

    }

    private fun startCounterProgressAnimation(seconds: Int) {

        binding?.progress?.max = seconds * 1000

        val animator = ValueAnimator.ofInt(0, seconds * 1000)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { binding?.progress?.progress = it.animatedValue as Int }
        animator.interpolator = LinearInterpolator()

        lifecycle.addObserver(LifecycleValueAnimator(animator))

    }

    private fun startCounter(
        seconds: Int,
        onCounterEnd: suspend () -> Unit
    ) {

        startCounterAnimation(seconds)
        startCounterProgressAnimation(seconds)

        lifecycleScope.launchSafe {

            delay(seconds * 1000L + 300L)
            onCounterEnd()

        }

    }

}