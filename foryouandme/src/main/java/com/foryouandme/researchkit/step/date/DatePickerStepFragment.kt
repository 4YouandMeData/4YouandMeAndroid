package com.foryouandme.researchkit.step.date

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.foryouandme.databinding.StepDateBinding
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.date.compose.DatePickerPage
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DatePickerStepFragment : StepFragment() {

    private val viewModel: DatePickerViewModel by viewModels()

    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                DatePickerPage(
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

        val step = taskViewModel.getStepByIndexAs<DatePickerStep>(indexArg())
        if(step != null) viewModel.execute(DatePickerAction.SetStep(step))

    }
}