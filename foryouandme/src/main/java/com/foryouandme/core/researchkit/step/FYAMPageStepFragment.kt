package com.foryouandme.core.researchkit.step

import android.os.Bundle
import android.view.View
import com.foryouandme.R
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.step.StepFragment
import kotlinx.android.synthetic.main.step_fyam_page.*


class FYAMPageStepFragment : StepFragment(R.layout.step_fyam_page) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<FYAMPageStep>(indexArg())

        step?.let { applyData(it) }
    }

    private fun applyData(
        step: FYAMPageStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        startCoroutineAsync {

            val pageData =
                if (step.remind)
                    step.page.copy(
                        specialLinkLabel = step.configuration.text.task.remindButton,
                        specialLinkValue = "remind"
                    )
                else
                    step.page

            val specialAction: ((String) -> Unit)? =
                if (step.remind) {
                    { startCoroutineAsync { viewModel.reschedule(taskNavController()) } }
                } else null

            page.applyData(
                configuration = step.configuration,
                page = pageData,
                pageType = step.pageType,
                action1 = { startCoroutineAsync { next() } },
                action2 = null,
                extraStringAction = null,
                extraPageAction = null,
                specialStringAction = specialAction

            )

        }

    }

}