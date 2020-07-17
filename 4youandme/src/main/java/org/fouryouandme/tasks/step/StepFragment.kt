package org.fouryouandme.tasks.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.tasks.TaskViewModel
import org.fouryouandme.tasks.step.introduction.IntroductionStepView

class StepFragment : BaseFragment<TaskViewModel>() {

    private val args: StepFragmentArgs by navArgs()

    override val viewModel: TaskViewModel by lazy {

        viewModelFactory(
            requireParentFragment().requireParentFragment(),
            getFactory { TaskViewModel(navigator, IORuntime) }
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        viewModel.getStepByIndex(args.index)
            .map { step ->

                when (step) {
                    is Step.IntroductionStep ->
                        IntroductionStepView(requireContext())
                }
            }
            .orNull()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStepByIndex(args.index)
            .map { step ->

                when (step) {
                    is Step.IntroductionStep ->
                        (view as IntroductionStepView).applyData(step)
                }
            }
            .orNull()

    }
}