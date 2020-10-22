package org.fouryouandme.tasks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.task.*
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.ext.evalOnMain
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync
import java.io.File

class TaskFragment : BaseFragment<TaskViewModel>(R.layout.task) {

    override val viewModel: TaskViewModel by lazy {
        viewModelFactory(this, getFactory {
            TaskViewModel(
                navigator,
                taskConfiguration()
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateLiveData()
            .observeEvent(name()) { stateUpdate ->
                when (stateUpdate) {
                    is TaskStateUpdate.Initialization ->
                        startCoroutineAsync { applyData() }
                    is TaskStateUpdate.Completed ->
                        startCoroutineAsync { viewModel.close(taskNavController()) }
                }
            }

        viewModel.loadingLiveData()
            .observeEvent(name()) {
                when (it.task) {
                    TaskLoading.Initialization ->
                        task_loading.setVisibility(it.active, false)
                    TaskLoading.Result ->
                        task_loading.setVisibility(it.active)
                }
            }

        viewModel.errorLiveData()
            .observeEvent(name()) {
                when (it.cause) {
                    TaskError.Initialization ->
                        errorAlert(it.error) {
                            viewModel.initialize(
                                typeArg(),
                                idArg(),
                                dataArg()
                            )
                        }
                    TaskError.Result -> errorAlert(it.error) { viewModel.end() }
                }
            }

        clearSensorFolder()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            if (viewModel.isInitialized().not())
                viewModel.initialize(typeArg(), idArg(), dataArg())
            else
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


    private fun errorAlert(error: FourYouAndMeError, retry: suspend () -> Unit): Unit {

        AlertDialog.Builder(requireContext())
            .setTitle(error.title(requireContext()))
            .setMessage(error.message(requireContext()))
            .setPositiveButton(R.string.TASK_error_retry)
            { dialog, _ ->
                startCoroutineAsync { retry() }
                dialog.dismiss()
            }
            .setNegativeButton(R.string.TASK_error_cancel)
            { _, _ ->
                startCoroutineAsync {
                    viewModel.close(taskNavController())
                }
            }
            .setCancelable(false)
            .show()

    }

    private fun clearSensorFolder(): Unit {

        val dir = getSensorOutputDirectory()

        if (dir.exists())
            dir.deleteRecursively()

    }

    /**
     * @return directory for outputting data logger files
     */
    fun getSensorOutputDirectory(): File =
        File("${requireContext().applicationContext.filesDir.absolutePath}/sensors")

    private fun typeArg(): String = arguments?.getString(TASK_TYPE, null)!!

    private fun idArg(): String = arguments?.getString(TASK_ID, null)!!

    @Suppress("UNCHECKED_CAST")
    private fun dataArg(): HashMap<String, String> =
        (arguments?.getSerializable(TASK_DATA) as HashMap<String, String>)

    fun taskNavController(): TaskNavController = TaskNavController(findNavController())

    companion object {

        const val TASK_TYPE = "type"

        private const val TASK_ID = "id"

        private const val TASK_DATA = "data"

        fun getBundle(type: String, id: String, data: HashMap<String, String>): Bundle {

            val bundle = Bundle()
            bundle.putString(TASK_TYPE, type)
            bundle.putString(TASK_ID, id)
            bundle.putSerializable(TASK_DATA, data)
            return bundle

        }

    }
}