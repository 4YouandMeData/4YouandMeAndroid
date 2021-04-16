package com.foryouandme.researchkit.step.holepeg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.entity.task.result.holepeg.HolePegResult
import com.foryouandme.researchkit.step.StepFragment
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZonedDateTime

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
                HolePeg { complete(it) }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = taskViewModel.getStepByIndexAs<HolePegStep>(indexArg())
        viewModel.execute(HolePegAction.SetStep(step))

    }

    private fun complete(state: HolePegState) {

        val step = taskViewModel.getStepByIndexAs<HolePegStep>(indexArg())
        if (step != null) {
            addResult(
                HolePegResult(
                    step.identifier,
                    state.start,
                    ZonedDateTime.now(),
                    state.attempts
                )
            )
        }
        next()

    }

}