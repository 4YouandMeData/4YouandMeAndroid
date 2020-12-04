package com.foryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.core.ext.navigator
import com.foryouandme.researchkit.task.TaskInjector
import com.foryouandme.ui.tasks.TaskFragment
import com.foryouandme.ui.tasks.TaskViewModel

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
