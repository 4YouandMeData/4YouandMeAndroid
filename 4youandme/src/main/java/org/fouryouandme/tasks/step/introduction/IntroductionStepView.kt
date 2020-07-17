package org.fouryouandme.tasks.step.introduction

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.page.view.title
import kotlinx.android.synthetic.main.step_introduction.view.*
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.tasks.step.Step
import kotlin.math.roundToInt


class IntroductionStepView(context: Context) : FrameLayout(context) {

    init {

        View.inflate(context, R.layout.step_introduction, this)

    }

    fun applyData(
        step: Step.IntroductionStep,
        nextAction: () -> Unit
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        val lp = image.layoutParams
        val displayMetrics = DisplayMetrics()

        (context as FragmentActivity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)

        val height = displayMetrics.heightPixels
        lp.height = (height * 0.4).roundToInt()
        image.layoutParams = lp
        image.setImageResource(step.image)
        image.setBackgroundColor(Color.argb(255, 227, 227, 227))

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.text = step.description
        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        next.background = button(step.configuration.theme.primaryColorEnd.color())
        next.text = step.button
        next.setTextColor(step.configuration.theme.secondaryColor.color())
        next.setOnClickListener { nextAction() }

    }
}