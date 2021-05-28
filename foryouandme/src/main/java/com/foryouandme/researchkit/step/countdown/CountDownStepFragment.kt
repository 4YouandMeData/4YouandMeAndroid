package com.foryouandme.researchkit.step.countdown

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_countdown.*
import kotlinx.coroutines.delay

class CountDownStepFragment : StepFragment(R.layout.step_countdown) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            taskViewModel.getStepByIndexAs<CountDownStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: CountDownStep
    ) {

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title(requireContext())
        title.setTextColor(step.titleColor)

        description.text = step.description(requireContext())
        description.setTextColor(step.descriptionColor)

        counter.setTextColor(step.counterColor)

        progress.progressTintList =
            ColorStateList.valueOf(step.counterProgressColor)

        startCounter(step.seconds) { next() }
    }

    private fun startCounterAnimation(seconds: Int) {

        val animator = ValueAnimator.ofInt(seconds, 0)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { counter.text = (it.animatedValue as Int).toString() }
        animator.interpolator = LinearInterpolator()

        lifecycle.addObserver(LifecycleValueAnimator(animator))

    }

    private fun startCounterProgressAnimation(seconds: Int) {

        progress.max = seconds * 1000

        val animator = ValueAnimator.ofInt(0, seconds * 1000)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { progress.progress = it.animatedValue as Int }
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