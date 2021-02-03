package com.foryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.ui.tasks.TaskFragment
import com.foryouandme.ui.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepContainerFragment : BaseFragment(R.layout.step) {

    private val args: StepContainerFragmentArgs by navArgs()

    val viewModel: TaskViewModel by viewModels({ taskFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step = viewModel.getStepByIndex(args.index)

        val fragment =
            step?.let { it.view() }?.let { StepFragment.buildWithParams(args.index, it) }

        if (step != null && fragment != null) {

            val transaction = childFragmentManager.beginTransaction()
            transaction.add(R.id.step_root, fragment, step.identifier)
            transaction.commit()

        }

    }

    private fun taskFragment(): TaskFragment = find()

}
