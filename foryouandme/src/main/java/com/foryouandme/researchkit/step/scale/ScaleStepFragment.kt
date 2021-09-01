package com.foryouandme.researchkit.step.scale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.scale.compose.ScalePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScaleStepFragment : StepFragment() {

    private val viewModel: ScaleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ScalePage(
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

        val step = taskViewModel.getStepByIndexAs<ScaleStep>(indexArg())
        if (step != null) viewModel.execute(ScaleAction.SetStep(step))

    }

}