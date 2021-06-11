package com.foryouandme.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.arch.flow.observeIn
import com.foryouandme.core.arch.flow.unwrapEvent
import com.foryouandme.core.ext.catchToNull
import com.foryouandme.databinding.TaskBinding
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.researchkit.step.StepNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import java.io.File

@AndroidEntryPoint
class TaskFragment : BaseFragment(R.layout.task) {

    private val viewModel: TaskViewModel by viewModels()

    val binding: TaskBinding?
        get() = view?.let { TaskBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.stateUpdate
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is TaskStateUpdate.Initialization -> applyData()
                    is TaskStateUpdate.Completed,
                    is TaskStateUpdate.Cancelled,
                    TaskStateUpdate.Rescheduled -> navigator.back(rootNavController())
                }
            }
            .observeIn(this)

        viewModel.loading
            .unwrapEvent(name)
            .onEach {
                when (it.task) {
                    TaskLoading.Initialization ->
                        binding?.taskLoading?.setVisibility(it.active, false)
                    TaskLoading.Reschedule ->
                        binding?.taskLoading?.setVisibility(it.active)
                    TaskLoading.Result ->
                        binding?.taskLoading?.setVisibility(it.active)
                }
            }
            .observeIn(this)

        viewModel.error
            .unwrapEvent(name)
            .onEach {

                binding?.taskLoading?.setVisibility(false)

                when (it.cause) {
                    TaskError.Initialization ->
                        errorAlert(it.error)
                        { viewModel.execute(TaskStateEvent.Initialize(idArg(), dataArg())) }
                    TaskError.Reschedule ->
                        errorAlert(it.error) { viewModel.execute(TaskStateEvent.Reschedule) }
                    TaskError.Result ->
                        errorAlert(it.error) { viewModel.execute(TaskStateEvent.End) }
                }
            }
            .observeIn(this)

        viewModel.navigation
            .unwrapEvent(name)
            .onEach {
                when (it) {
                    is StepToStep ->
                        navigator.navigateTo(stepNavController(), it)
                    else ->
                        navigator.navigateTo(rootNavController(), it)
                }

            }
            .observeIn(this)

        clearSensorFolder()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.state.task == null)
            viewModel.execute(TaskStateEvent.Initialize(idArg(), dataArg()))

    }

    private fun applyData() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val currentGraph = catchToNull { navHostFragment.navController.graph }
        if (currentGraph == null) {
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.task_navigation)
            val navController = navHostFragment.navController

            val destination = R.id.step
            navGraph.setStartDestination(destination)
            navController.graph = navGraph
        }

    }

    private fun errorAlert(error: Throwable, retry: () -> Unit) {

        val title = errorMessenger.getTitle()
        val message = errorMessenger.getMessage(error, viewModel.state.configuration)

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.TASK_error_retry)
            { dialog, _ ->
                retry()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.TASK_error_cancel)
            { _, _ -> navigator.back(rootNavController()) }
            .setCancelable(false)
            .show()

    }

    private fun clearSensorFolder() {

        val dir = getSensorOutputDirectory()

        if (dir.exists()) dir.deleteRecursively()

    }

    /**
     * @return directory for outputting data logger files
     */
    fun getSensorOutputDirectory(): File =
        File("${requireContext().applicationContext.filesDir.absolutePath}/sensors")

    private fun idArg(): String = arguments?.getString(TASK_ID, null)!!

    @Suppress("UNCHECKED_CAST")
    private fun dataArg(): HashMap<String, String> =
        (arguments?.getSerializable(TASK_DATA) as HashMap<String, String>)

    private fun stepNavController(): StepNavController =
        (childFragmentManager.fragments[0]
            .childFragmentManager.fragments[0]
            .childFragmentManager.fragments[0] as StepFragment).stepNavController()

    companion object {

        private const val TASK_ID = "id"

        private const val TASK_DATA = "data"

        fun getBundle(id: String, data: HashMap<String, String>): Bundle {

            val bundle = Bundle()
            bundle.putString(TASK_ID, id)
            bundle.putSerializable(TASK_DATA, data)
            return bundle

        }

    }
}