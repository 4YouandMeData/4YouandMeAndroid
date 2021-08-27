package com.foryouandme.researchkit.step.textinput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.textinput.compose.TextInputPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextInputStepFragment : StepFragment() {

    private val viewModel: TextInputViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                TextInputPage(
                    viewModel = viewModel,
                    onNext = {
                        if (it != null) addResult(it)
                        next()
                    }
                )
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<TextInputStep>(indexArg())
        if (step != null) viewModel.execute(TextInputAction.SetStep(step))

    }

}