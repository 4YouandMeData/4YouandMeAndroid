package com.foryouandme.core.researchkit.step.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.databinding.StepFyamPageBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FYAMPageStepFragment : StepFragment() {

    private val viewModel: FYAMPageStepViewModel by viewModels()

    val binding: StepFyamPageBinding?
        get() = view?.let { StepFyamPageBinding.bind(it) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                FYAMPageStepPage(
                    viewModel = viewModel,
                    next = { next() },
                    reschedule = { reschedule() }
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<FYAMPageStep>(indexArg())
        if(step != null) viewModel.execute(FYAMPageStepAction.SetStep(step))

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