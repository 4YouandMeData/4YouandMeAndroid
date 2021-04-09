package com.foryouandme.researchkit.step.nineholepeg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.countdown.CountDownStep
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NineHolePegFragment : StepFragment() {

    private val viewModel: NineHolePegViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                NineHolePeg()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<NineHolePegStep>(indexArg())
        viewModel.execute(NineHolePegSateEvent.SetStep(step))

    }


}