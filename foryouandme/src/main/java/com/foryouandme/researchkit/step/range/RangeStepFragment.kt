package com.foryouandme.researchkit.step.range

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.range.compose.RangePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RangeStepFragment : StepFragment() {

    private val viewModel: RangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                RangePage(
                    viewModel = viewModel,
                    onNext = {
                        if (it != null) addResult(it)
                        next()
                    },
                    onSkip = { result, target ->
                        if (result != null) addResult(result)
                        skipTo(target)
                    }
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<RangeStep>(indexArg())
        if (step != null) viewModel.execute(RangeAction.SetStep(step))

    }

}