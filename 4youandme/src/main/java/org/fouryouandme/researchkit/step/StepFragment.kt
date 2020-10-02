package org.fouryouandme.researchkit.step

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import org.fouryouandme.R
import org.fouryouandme.core.arch.android.BaseFragment
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.find
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.task.TaskInjector
import org.fouryouandme.tasks.TaskFragment
import org.fouryouandme.tasks.TaskNavController
import org.fouryouandme.tasks.TaskViewModel

open class StepFragment(contentLayoutId: Int) : BaseFragment<TaskViewModel>(contentLayoutId) {

    override val viewModel by lazy {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    showCancelDialog()
                }

            }
        )
    }

    protected fun indexArg(): Int =
        arguments?.getInt(INDEX, -1)
            ?.let { if (it == -1) null else it }!!

    protected open suspend fun next(): Unit {
        viewModel.nextStep(stepNavController(), indexArg())
    }

    protected fun taskFragment(): TaskFragment = find()

    protected fun taskNavController(): TaskNavController =
        taskFragment().taskNavController()

    protected fun stepNavController(): StepNavController = StepNavController(findNavController())

    protected fun showCancelDialog(): Unit {

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.TASK_cancel_title)
            .setMessage(R.string.TASK_cancel_description)
            .setPositiveButton(R.string.TASK_cancel_positive)
            { _, _ ->
                startCoroutineAsync {
                    viewModel.cancel()
                    viewModel.close(taskNavController())
                }
            }
            .setNegativeButton(R.string.TASK_cancel_negative)
            { dialog, _ -> dialog.dismiss() }
            .show()

    }

    companion object {

        private const val INDEX = "index"

        fun <T : StepFragment> buildWithParams(index: Int, fragment: T): T {

            val bundle = Bundle()
            bundle.putInt(INDEX, index)
            fragment.arguments = bundle

            return fragment

        }

    }
}