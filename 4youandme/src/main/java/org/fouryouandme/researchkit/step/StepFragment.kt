package org.fouryouandme.researchkit.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toT
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.researchkit.step.active.ActiveStepView
import org.fouryouandme.researchkit.step.countdown.CountDownStepView
import org.fouryouandme.researchkit.step.introduction.IntroductionStepView
import org.fouryouandme.tasks.TaskViewModel

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
                    is Step.CountDownStep ->
                        CountDownStepView(requireContext())
                    is Step.ActiveStep ->
                        ActiveStepView(requireContext())
                }
            }
            .orNull()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.getStepByIndex(args.index) toT !viewModel.state().task }
            .map { (step, task) ->

                when (step) {
                    is Step.IntroductionStep ->
                        (view as IntroductionStepView).applyData(step) {
                            viewModel.nextStep(
                                findNavController(),
                                args.index
                            )
                        }
                    is Step.CountDownStep ->
                        (view as CountDownStepView).applyData(step) {
                            viewModel.nextStep(
                                findNavController(),
                                args.index
                            )
                        }
                    is Step.ActiveStep ->
                        (view as ActiveStepView).applyData(step, task)
                }
            }
            .orNull()

    }
}