package com.foryouandme.researchkit.step.trailmaking

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.foryouandme.R
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.databinding.StepTrailMakingBinding
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TrailMakingStepFragment : StepFragment(R.layout.step_trail_making) {

    private val viewModel: TrailMakingViewModel by viewModels()

    private val binding: StepTrailMakingBinding?
        get() = view?.let { StepTrailMakingBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    TrailMakingStateUpdate.Initialized -> applyData()
                }
            }
            .observeIn(this)

        if (viewModel.state.points.isEmpty())
            viewModel.execute(TrailMakingStateEvent.Initialize)
        else
            applyData()

    }

    private fun applyData() {

        val step = taskViewModel.getStepByIndexAs<TrailMakingStep>(indexArg())
        val viewBinding = binding

        if (viewBinding != null && step != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

        }

    }


}