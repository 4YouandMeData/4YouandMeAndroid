package org.fouryouandme.researchkit.step

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import arrow.core.None
import arrow.core.some
import arrow.core.toOption
import org.fouryouandme.core.arch.android.getFactory
import org.fouryouandme.core.arch.android.viewModelFactory
import org.fouryouandme.core.ext.IORuntime
import org.fouryouandme.core.ext.navigator
import org.fouryouandme.core.ext.sectionParent
import org.fouryouandme.tasks.TaskViewModel

open class StepFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected val viewModel by lazy {
        viewModelFactory(
            taskFragment(),
            getFactory { TaskViewModel(navigator, IORuntime) }
        )
    }

    protected fun indexArg(): Int =
        arguments?.getInt(INDEX, -1)
            .toOption()
            .flatMap { if (it == -1) None else it.some() }
            .orNull()!!

    protected open fun next(): Unit =
        viewModel.nextStep(sectionParent().findNavController(), indexArg())

    protected fun taskFragment(): Fragment = sectionParent().requireParentFragment()

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