package org.fouryouandme.researchkit.step.end

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import kotlinx.android.synthetic.main.step_end.*
import kotlinx.android.synthetic.main.step_introduction.description
import kotlinx.android.synthetic.main.step_introduction.next
import kotlinx.android.synthetic.main.step_introduction.root
import kotlinx.android.synthetic.main.step_introduction.title
import org.fouryouandme.R
import org.fouryouandme.core.entity.configuration.button.button
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.step.StepFragment


class EndStepFragment : StepFragment(R.layout.step_end) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<EndStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: EndStep
    ): Unit {

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title
        title.setTextColor(step.titleColor)

        description.text = step.description
        description.setTextColor(step.descriptionColor)

        next.background = button(step.buttonColor)
        next.text = step.button
        next.setTextColor(step.buttonTextColor)
        next.setOnClickListener { startCoroutineAsync { next(step.close) } }

        animationView.addValueCallback(
            KeyPath("Shape Outlines", "**"),
            LottieProperty.COLOR_FILTER,
            {
                PorterDuffColorFilter(
                    step.checkMarkBackgroundColor,
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        )

        animationView.addValueCallback(
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