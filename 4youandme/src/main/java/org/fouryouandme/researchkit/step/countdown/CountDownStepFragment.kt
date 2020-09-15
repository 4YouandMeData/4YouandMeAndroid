package org.fouryouandme.researchkit.step.countdown

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import arrow.core.None
import arrow.core.Option
import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlinx.android.synthetic.main.step_countdown.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.step.countdown.LifecycleIO.Companion.bindToLifecycle


class CountDownStepFragment : StepFragment(R.layout.step_countdown) {

    private var counterAnimator: Option<ValueAnimator> = None
    private var progressAnimator: Option<ValueAnimator> = None

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.CountDownStep>(indexArg())

        step.map { applyData(it) }
    }

    private fun applyData(
        step: Step.CountDownStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        counter.setTextColor(step.configuration.theme.primaryTextColor.color())

        progress.progressTintList =
            ColorStateList.valueOf(step.configuration.theme.primaryColorEnd.color())

        startCounter(step.seconds) { next() }
    }

    private fun startCounterAnimation(seconds: Int): Unit {

        val animator = ValueAnimator.ofInt(seconds, 0)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { counter.text = (it.animatedValue as Int).toString() }
        animator.interpolator = LinearInterpolator()

        lifecycle.addObserver(LifecycleValueAnimator(animator))

    }

    private fun startCounterProgressAnimation(seconds: Int): Unit {

        progress.max = seconds * 1000

        val animator = ValueAnimator.ofInt(0, seconds * 1000)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { progress.progress = it.animatedValue as Int }
        animator.interpolator = LinearInterpolator()

        lifecycle.addObserver(LifecycleValueAnimator(animator))

    }

    private fun startCounter(
        seconds: Int,
        onCounterEnd: () -> Unit
    ): Unit {

        startCounterAnimation(seconds)
        startCounterProgressAnimation(seconds)

        IO.fx {

            !effect { delay(seconds * 1000L + 300L) }
            continueOn(Dispatchers.Main)
            onCounterEnd()

        }.bindToLifecycle(this)

    }

}