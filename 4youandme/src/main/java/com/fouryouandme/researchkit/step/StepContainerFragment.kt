package com.fouryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.fouryouandme.R
import com.fouryouandme.core.arch.android.BaseFragment
import com.fouryouandme.core.arch.android.getFactory
import com.fouryouandme.core.arch.android.viewModelFactory
import com.fouryouandme.core.ext.find
import com.fouryouandme.core.ext.mapNotNull
import com.fouryouandme.core.ext.navigator
import com.fouryouandme.researchkit.task.TaskInjector
import com.fouryouandme.tasks.TaskFragment
import com.fouryouandme.tasks.TaskViewModel

class StepContainerFragment : BaseFragment<TaskViewModel>(R.layout.step) {

    private val args: StepContainerFragmentArgs by navArgs()

    override val viewModel: TaskViewModel by lazy {

        viewModelFactory(
            taskFragment(),
            getFactory {
                TaskViewModel(
                    navigator,
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
