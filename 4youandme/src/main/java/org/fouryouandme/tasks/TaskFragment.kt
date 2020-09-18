package org.fouryouandme.tasks

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.*
import org.fouryouandme.researchkit.task.ETaskType

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory {
            TaskViewModel(
                navigator,
                IORuntime,
                injector.configurationModule()
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
                viewModel.initialize(typeArg())

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

    private fun typeArg(): ETaskType = arguments?.getSerializable(TASK_TYPE) as ETaskType

    fun navController(): TaskNavController = TaskNavController(findNavController())

    companion object {

        const val TASK_TYPE = "type"

        fun getBundle(type: ETaskType): Bundle {

            val bundle = Bundle()
            bundle.putSerializable(TASK_TYPE, type)
            return bundle

        }

    }
}