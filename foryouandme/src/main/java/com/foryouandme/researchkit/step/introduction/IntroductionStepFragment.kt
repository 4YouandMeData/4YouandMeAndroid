package com.foryouandme.researchkit.step.introduction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.introduction.compose.Introduction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroductionStepFragment : StepFragment() {

    private val viewModel: IntroductionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                Introduction { next() }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<IntroductionStep>(indexArg())
        viewModel.execute(IntroductionAction.SetStep(step))

    }

}