package org.fouryouandme.researchkit.step.video

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.ext.hide
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment

class VideoDiaryStepFragment : StepFragment(R.layout.step_video_diary) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.VideoDiaryStep>(indexArg())

        step.map { applyData(it) }

    }

    private fun applyData(
        step: Step.VideoDiaryStep
    ): Unit {

        taskFragment().toolbar.apply {

            hide()

        }

    }

}