package com.foryouandme.researchkit.step

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foryouandme.R
import com.foryouandme.core.arch.android.BaseFragment
import com.foryouandme.core.ext.find
import com.foryouandme.core.ext.hideKeyboard
import com.foryouandme.researchkit.result.StepResult
import com.foryouandme.ui.tasks.TaskFragment
import com.foryouandme.ui.tasks.TaskStateEvent
import com.foryouandme.ui.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class StepFragment(contentLayoutId: Int) : BaseFragment(contentLayoutId) {

    val taskViewModel: TaskViewModel by viewModels({ taskFragment() })

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

        taskViewModel.getStepByIndex(indexArg())?.let {

            if (taskViewModel.canGoBack(indexArg()) && indexArg() != 0 && it.back != null)
                showBack(it.back.image)
            else
                hideToolbar()

            if (it.skip != null) showSkip(it.skip.text, it.skip.color)
            else hideSkip()
        }

    }

    protected fun indexArg(): Int =
        arguments?.getInt(INDEX, -1)
            ?.let { if (it == -1) null else it }!!

    protected open fun next() {
        hideKeyboard()
        taskViewModel.execute(TaskStateEvent.NextStep(indexArg()))
    }

    protected open fun skipTo(stepId: String?) {
        hideKeyboard()
        taskViewModel.execute(TaskStateEvent.SkipToStep(stepId, indexArg()))
    }

    protected open fun reschedule() {
        hideKeyboard()
        taskViewModel.execute(TaskStateEvent.Reschedule)
    }

    protected open fun addResult(result: StepResult) {
        taskViewModel.execute(TaskStateEvent.AddResult(result))
    }

    protected open fun end() {
        hideKeyboard()
        taskViewModel.execute(TaskStateEvent.End)
    }

    protected open fun close() {
        hideKeyboard()
        navigator.back(rootNavController())
    }

    protected fun taskFragment(): TaskFragment = find()

    fun stepNavController(): StepNavController = StepNavController(findNavController())

    protected fun showCancelDialog() {

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.TASK_cancel_title)
            .setMessage(R.string.TASK_cancel_description)
            .setPositiveButton(R.string.TASK_cancel_positive)
            { _, _ -> taskViewModel.execute(TaskStateEvent.Cancel) }
            .setNegativeButton(R.string.TASK_cancel_negative)
            { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun showBack(image: Int) {

        taskFragment().binding?.toolbar?.apply {

            setNavigationIcon(image)
            setNavigationOnClickListener {

                hideKeyboard()
                if (navigator.back(stepNavController()).not())
                    navigator.back(rootNavController())

            }

            visibility = View.VISIBLE

        }

    }

    private fun showSkip(skipText: String, color: Int) {

        taskFragment().binding?.skip?.apply {

            text = skipText
            setTextColor(color)
            visibility = View.VISIBLE
            setOnClickListener { next() }

        }

    }

    private fun hideSkip() {

        taskFragment().binding?.skip?.visibility = View.GONE

    }

    private fun hideToolbar() {

        taskFragment().binding?.toolbar?.visibility = View.INVISIBLE

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