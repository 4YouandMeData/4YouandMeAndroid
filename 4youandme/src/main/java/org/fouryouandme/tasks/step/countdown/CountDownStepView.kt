package org.fouryouandme.tasks.step.countdown

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import arrow.fx.IO
import arrow.fx.extensions.io.concurrent.concurrent
import kotlinx.android.synthetic.main.page.view.title
import kotlinx.android.synthetic.main.step_countdown.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.fouryouandme.R
import org.fouryouandme.core.ext.unsafeRunAsync
import org.fouryouandme.tasks.step.Step


class CountDownStepView(context: Context) : FrameLayout(context) {

    init {

        View.inflate(context, R.layout.step_countdown, this)

    }

    fun applyData(
        step: Step.CountDownStep,
        onCounterEnd: () -> Unit
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        counter.setTextColor(step.configuration.theme.primaryTextColor.color())

        progress.progressTintList =
            ColorStateList.valueOf(step.configuration.theme.primaryColorEnd.color())

        startCounter(step.seconds, onCounterEnd)
    }

    private fun startCounterAnimation(seconds: Int): Unit {

        val animator = ValueAnimator.ofInt(seconds, 0)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { counter.text = (it.animatedValue as Int).toString() }
        animator.interpolator = LinearInterpolator()
        animator.start()

    }

    private fun startCounterProgressAnimation(seconds: Int): Unit {

        progress.max = seconds * 1000

        val animator = ValueAnimator.ofInt(0, seconds * 1000)

        animator.duration = seconds * 1000L
        animator.addUpdateListener { progress.progress = it.animatedValue as Int }
        animator.interpolator = LinearInterpolator()
        animator.start()

    }

    private fun startCounter(
        seconds: Int,
        onCounterEnd: () -> Unit
    ): Unit {

        startCounterAnimation(seconds)
        startCounterProgressAnimation(seconds)

        IO.concurrent().fx.concurrent {

            !effect { delay(seconds * 1000L + 300L) }
            continueOn(Dispatchers.Main)
            onCounterEnd()

        }.unsafeRunAsync()

    }
}