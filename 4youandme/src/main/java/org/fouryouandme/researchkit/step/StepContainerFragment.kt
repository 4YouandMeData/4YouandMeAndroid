package org.fouryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import arrow.core.Option
import arrow.core.Tuple3
import arrow.core.extensions.fx
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.sectionParent
import org.fouryouandme.researchkit.step.countdown.CountDownStepFragment
import org.fouryouandme.researchkit.step.end.EndStepFragment
import org.fouryouandme.researchkit.step.introduction.IntroductionListStepFragment
import org.fouryouandme.researchkit.step.introduction.IntroductionStepFragment
import org.fouryouandme.researchkit.step.sensor.SensorStepFragment
import org.fouryouandme.researchkit.step.start.StartStepFragment
import org.fouryouandme.researchkit.step.video.VideoDiaryStepFragment
import org.fouryouandme.tasks.TaskViewModel

class StepContainerFragment : BaseFragment<TaskViewModel>(R.layout.step) {

    private val args: StepContainerFragmentArgs by navArgs()

    override val viewModel: TaskViewModel by lazy {

        viewModelFactory(
            sectionParent(),
            getFactory { TaskViewModel(navigator, IORuntime) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndex(args.index)

        val fragment =
            step.map {

                when (it) {
                    is Step.StartStep ->
                        StartStepFragment()
                    is Step.IntroductionStep ->
                        IntroductionStepFragment()
                    is Step.IntroductionListStep ->
                        IntroductionListStepFragment()
                    is Step.CountDownStep ->
                        CountDownStepFragment()
                    is Step.EndStep ->
                        EndStepFragment()
                    is Step.SensorStep ->
                        SensorStepFragment()
                    is Step.VideoDiaryStep ->
                        VideoDiaryStepFragment()
                }
            }.map { StepFragment.buildWithParams(args.index, it) }

        Option.fx { Tuple3(!step, !fragment, !viewModel.state().task) }
            .map {

                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.step_root, it.b, it.a.identifier)
                transaction.commit()

            }
    }
}
