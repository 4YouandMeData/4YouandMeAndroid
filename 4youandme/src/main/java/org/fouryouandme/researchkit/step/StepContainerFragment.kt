package org.fouryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.*
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
            getFactory { TaskViewModel(navigator, IORuntime, injector.configurationModule()) }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndex(args.index)

        val fragment =
            step?.let {

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
            }?.let { StepFragment.buildWithParams(args.index, it) }

        mapNotNull(step, fragment) { s, f ->

            val transaction = childFragmentManager.beginTransaction()
            transaction.add(R.id.step_root, f, s.identifier)
            transaction.commit()

        }
    }
}
