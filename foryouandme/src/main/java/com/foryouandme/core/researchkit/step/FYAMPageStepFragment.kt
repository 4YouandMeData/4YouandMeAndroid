package com.foryouandme.core.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.databinding.StepFyamPageBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FYAMPageStepFragment : StepFragment(R.layout.step_fyam_page) {

    val binding: StepFyamPageBinding?
        get() = view?.let { StepFyamPageBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStepByIndexAs<FYAMPageStep>(indexArg())?.let { applyData(it) }
    }

    private fun applyData(step: FYAMPageStep) {

        val viewBinding = binding

        viewBinding?.root?.setBackgroundColor(step.configuration.theme.secondaryColor.color())


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
                { reschedule() }
            } else null

        // TODO: remove coroutine for UI
        lifecycleScope.launchSafe {
            viewBinding?.page?.applyData(
                configuration = step.configuration,
                page = pageData,
                pageType = step.pageType,
                action1 = { next() },
                action2 = null,
                extraStringAction = null,
                extraPageAction = null,
                specialStringAction = specialAction

            )
        }

    }

}