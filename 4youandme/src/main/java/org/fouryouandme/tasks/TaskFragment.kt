package org.fouryouandme.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.task.TaskInjector

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory {
            TaskViewModel(
                navigator,
                IORuntime,
                (requireContext().applicationContext as TaskInjector).provideBuilder()
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(TaskFragment::class.java.simpleName) {
                when (it) {
                    is TaskStateUpdate.Initialization ->
                        startCoroutineAsync { applyData() }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(identifierArg())

            applyData()

        }

    }

    private suspend fun applyData(): Unit =
        evalOnMain {

            val navHostFragment =
                childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.task_navigation)
            val navController = navHostFragment.navController

            val destination = R.id.step
            navGraph.startDestination = destination
            navController.graph = navGraph

        }

    private fun identifierArg(): String = arguments?.getString(TASK_IDENTIFIER, null)!!

    fun navController(): TaskNavController = TaskNavController(findNavController())

    companion object {

        const val TASK_IDENTIFIER = "identifier"

        fun getBundle(identifier: String): Bundle {

            val bundle = Bundle()
            bundle.putString(TASK_IDENTIFIER, identifier)
            return bundle

        }

    }
}