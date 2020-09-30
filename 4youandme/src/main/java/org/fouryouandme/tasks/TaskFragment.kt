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

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory {
            TaskViewModel(
                navigator,
                IORuntime,
                taskConfiguration()
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(TaskFragment::class.java.simpleName) { stateUpdate ->
                when (stateUpdate) {
                    is TaskStateUpdate.Initialization ->
                        startCoroutineAsync { applyData() }
                    is TaskStateUpdate.Completed ->
                        startCoroutineAsync { viewModel.close(taskNavController()) }
                }
            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(typeArg(), idArg())

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

    private fun typeArg(): String = arguments?.getString(TASK_TYPE, null)!!
    private fun idArg(): String = arguments?.getString(TASK_ID, null)!!

    fun taskNavController(): TaskNavController = TaskNavController(findNavController())

    companion object {

        const val TASK_TYPE = "type"

        private const val TASK_ID = "id"

        fun getBundle(type: String, id: String): Bundle {

            val bundle = Bundle()
            bundle.putString(TASK_TYPE, type)
            bundle.putString(TASK_ID, id)
            return bundle

        }

    }
}