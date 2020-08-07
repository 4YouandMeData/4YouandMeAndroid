package org.fouryouandme.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import arrow.core.Option
import arrow.core.extensions.fx
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.researchkit.task.Task

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory { TaskViewModel(navigator, IORuntime) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(TaskFragment::class.java.simpleName) {
                when (it) {
                    is TaskStateUpdate.Initialization ->
                        applyData(it.configuration, it.task)
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Option.fx { !viewModel.state().configuration to !viewModel.state().task }
            .fold(
                { viewModel.initialize() },
                { applyData(it.first, it.second) }
            )

    }

    private fun applyData(configuration: Configuration, task: Task): Unit {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.task_navigation)
        val navController = navHostFragment.navController

        val destination = R.id.step
        navGraph.startDestination = destination
        navController.graph = navGraph
    }
}