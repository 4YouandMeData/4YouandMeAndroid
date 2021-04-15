package com.foryouandme.researchkit.step.holepeg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HolePegFragment : StepFragment() {

    private val viewModel: HolePegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                HolePeg()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<HolePegStep>(indexArg())
        viewModel.execute(HolePegSateEvent.SetStep(step))

    }


}