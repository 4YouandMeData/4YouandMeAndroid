package org.fouryouandme.researchkit.step.sensor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.step_sensor.*
import org.fouryouandme.R
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.recorder.RecorderService
import org.fouryouandme.researchkit.recorder.RecorderServiceConnection
import org.fouryouandme.researchkit.recorder.RecordingState
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.tasks.TaskStateUpdate
import java.io.File

class SensorStepFragment : StepFragment(R.layout.step_sensor) {

    private val serviceConnection: RecorderServiceConnection =
        RecorderServiceConnection(
            { binder ->

                val step =
                    viewModel.getStepByIndexAs<Step.SensorStep>(indexArg())

                step?.let {

                    binder.bind(getOutputDirectory(), step, viewModel.state().task)

                    binder.stateLiveData()
                        .observeEvent(SensorStepFragment::class.java.simpleName) {

                            when (it) {
                                is RecordingState.Completed ->
                                    if (it.stepIdentifier == step.identifier)
                                        startCoroutineAsync { next() }
                                is RecordingState.Failure ->
                                    if (it.stepIdentifier == step.identifier)
                                        Toast.makeText(
                                            requireContext(),
                                            "Fallito",
                                            Toast.LENGTH_LONG
                                        ).show()
                            }

                        }

                }

                viewModel.stateLiveData()
                    .observeEvent(SensorStepFragment::class.java.simpleName) {
                        when (it) {
                            is TaskStateUpdate.Cancelled ->
                                if (it.isCancelled) binder.stop()
                        }
                    }
            },
            {}
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.SensorStep>(indexArg())

        step?.let { applyData(it) }
    }

    fun applyData(
        step: Step.SensorStep
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

    override fun onDestroy() {

        unbindServiceIO(serviceConnection)

        super.onDestroy()

    }
}