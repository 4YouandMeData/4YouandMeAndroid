package org.fouryouandme.researchkit.step.countdown

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import kotlinx.android.synthetic.main.step_countdown.*
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment

class CountDownStepFragment : StepFragment(R.layout.step_countdown) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<CountDownStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: CountDownStep
    ): Unit {

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title
        title.setTextColor(step.titleColor)

        description.text = step.description
        description.setTextColor(step.descriptionColor)

        counter.setTextColor(step.counterColor)

        progress.progressTintList =
            ColorStateList.valueOf(step.counterProgressColor)

        startCoroutineAsync { startCounter(step.seconds) { next() } }
    }

    private suspend fun startCounterAnimation(seconds: Int): Unit =
        evalOnMain {

            val animator = ValueAnimator.ofInt(seconds, 0)

            animator.duration = seconds * 1000L
            animator.addUpdateListener { counter.text = (it.animatedValue as Int).toString() }
            animator.interpolator = LinearInterpolator()

            lifecycle.addObserver(LifecycleValueAnimator(animator))

        }

    private suspend fun startCounterProgressAnimation(seconds: Int): Unit =
        evalOnMain {

            progress.max = seconds * 1000

            val animator = ValueAnimator.ofInt(0, seconds * 1000)

            animator.duration = seconds * 1000L
            animator.addUpdateListener { progress.progress = it.animatedValue as Int }
            animator.interpolator = LinearInterpolator()

            lifecycle.addObserver(LifecycleValueAnimator(animator))

        }

    private suspend fun startCounter(
        seconds: Int,
        onCounterEnd: suspend () -> Unit
    ): Unit {

        startCounterAnimation(seconds)
        startCounterProgressAnimation(seconds)

        LifecycleCoroutine.startLifecycleCoroutineAsync(this) {

            delay(seconds * 1000L + 300L)
            evalOnMain { onCounterEnd() }

        }

    }

}