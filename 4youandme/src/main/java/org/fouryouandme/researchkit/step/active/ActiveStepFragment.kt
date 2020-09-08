package org.fouryouandme.researchkit.step.active

import android.os.Bundle
import android.view.View
import android.widget.Toast
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toT
import kotlinx.android.synthetic.main.step_active.*
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.RecorderService
import org.fouryouandme.researchkit.recorder.RecorderServiceConnection
import org.fouryouandme.researchkit.recorder.RecordingState
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import java.io.File

class ActiveStepFragment : StepFragment(R.layout.step_active) {

    private val serviceConnection: RecorderServiceConnection =
        RecorderServiceConnection(
            { binder ->

                val stepOption =
                    viewModel.getStepByIndexAs<Step.ActiveStep>(indexArg())

                Option.fx { !stepOption toT !viewModel.state().task }
                    .map { (step, task) ->

                        binder.bind(getOutputDirectory(), step, task)
                    }

                viewModel.stateLiveData()

                binder.stateLiveData()
                    .observeEvent(ActiveStepFragment::class.java.simpleName) {

                        when (it) {
                            RecordingState.Completed -> next()
                            RecordingState.Failure ->
                                Toast.makeText(requireContext(), "Fallito", Toast.LENGTH_LONG)
                                    .show()
                        }

                    }

            },
            {}
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.ActiveStep>(indexArg())

        step.map { applyData(it) }
    }

    fun applyData(
        step: Step.ActiveStep
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.text = step.description
        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        RecorderService.start(requireContext().applicationContext, serviceConnection)

    }

    /**
     * @return directory for outputting data logger files
     */
    private fun getOutputDirectory(): File = requireContext().applicationContext.filesDir

}