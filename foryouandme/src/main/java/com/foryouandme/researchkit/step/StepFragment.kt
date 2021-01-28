package com.foryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragmentOld
import com.foryouandme.core.arch.android.getFactory
import com.foryouandme.core.arch.android.viewModelFactory
import com.foryouandme.core.ext.*
import com.foryouandme.researchkit.task.TaskInjector
import com.foryouandme.ui.tasks.TaskFragment
import com.foryouandme.ui.tasks.TaskNavController
import com.foryouandme.ui.tasks.TaskViewModel
import kotlinx.android.synthetic.main.task.*

open class StepFragment(contentLayoutId: Int) : BaseFragmentOld<TaskViewModel>(contentLayoutId) {

    override val viewModel by lazy {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startCoroutineAsync {

            viewModel.getStepByIndex(indexArg())?.let {

                if (viewModel.canGoBack(indexArg()) && indexArg() != 0 && it.back != null)
                    showBack(it.back.image)
                else
                    hideToolbar()

                if (it.skip != null) showSkip(it.skip.text, it.skip.color)
                else hideSkip()
            }

        }
    }

    protected fun indexArg(): Int =
        arguments?.getInt(INDEX, -1)
            ?.let { if (it == -1) null else it }!!

    protected open suspend fun next(): Unit {
        viewModel.nextStep(stepNavController(), indexArg())
    }

    protected open suspend fun skipTo(stepId: String?): Unit {
        viewModel.skipToStep(stepNavController(), stepId, indexArg())
    }

    protected open fun reschedule(): Unit {

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

    private suspend fun showBack(image: Int): Unit =
        evalOnMain {

            taskFragment().toolbar.apply {

                setNavigationIcon(image)
                setNavigationOnClickListener {

                    startCoroutineAsync {

                        viewModel.back(
                            indexArg(),
                            stepNavController(),
                            taskNavController()
                        )

                    }

                }

                visibility = View.VISIBLE

            }

        }

    private suspend fun showSkip(text: String, color: Int): Unit =
        evalOnMain {

            taskFragment().skip.text = text
            taskFragment().skip.setTextColor(color)
            taskFragment().skip.visibility = View.VISIBLE
            taskFragment().skip.setOnClickListenerAsync { next() }

        }

    private suspend fun hideSkip(): Unit =
        evalOnMain {

            taskFragment().skip.visibility = View.GONE

        }

    private suspend fun hideToolbar(): Unit =
        evalOnMain {

            taskFragment().toolbar.visibility = View.INVISIBLE

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