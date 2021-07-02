package com.foryouandme.researchkit.step.chooseone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.chooseone.compose.ChooseOnePage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseOneStepFragment : StepFragment() {

    private val viewModel: ChooseOneViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                ChooseOnePage(
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

        val step = taskViewModel.getStepByIndexAs<ChooseOneStep>(indexArg())
        if (step != null) viewModel.execute(ChooseOneAction.SetStep(step))

    }

}