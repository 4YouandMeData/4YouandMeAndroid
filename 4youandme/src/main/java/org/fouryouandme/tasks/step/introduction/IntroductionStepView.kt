package org.fouryouandme.tasks.step.introduction

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.page.view.title
import kotlinx.android.synthetic.main.step_introduction.view.*
import org.fouryouandme.R
import org.fouryouandme.tasks.step.Step


class IntroductionStepView(context: Context) : FrameLayout(context) {

    init {

        View.inflate(context, R.layout.step_introduction, this)

    }

    fun applyData(
        step: Step.IntroductionStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title

    }
}