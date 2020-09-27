package org.fouryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.mapNotNull
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.researchkit.task.TaskInjector
import org.fouryouandme.tasks.TaskFragment
import org.fouryouandme.tasks.TaskViewModel

class StepContainerFragment : BaseFragment<TaskViewModel>(R.layout.step) {

    private val args: StepContainerFragmentArgs by navArgs()

    override val viewModel: TaskViewModel by lazy {

        viewModelFactory(
            taskFragment(),
            getFactory {
                TaskViewModel(
                    navigator,
                    IORuntime,
                    (requireContext().applicationContext as TaskInjector).provideBuilder()
                )
            }
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndex(args.index)

        val fragment =
            step?.let { it.view() }?.let { StepFragment.buildWithParams(args.index, it) }

        mapNotNull(step, fragment)
            ?.let {

                val transaction = childFragmentManager.beginTransaction()
                transaction.add(R.id.step_root, it.b, it.a.identifier)
                transaction.commit()

            }
    }

    private fun taskFragment(): TaskFragment = find()
}
