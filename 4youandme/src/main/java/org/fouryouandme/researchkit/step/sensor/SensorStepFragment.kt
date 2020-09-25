package org.fouryouandme.researchkit.step.sensor

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.step_sensor.*
import org.fouryouandme.R
import org.fouryouandme.core.ext.infoToast
import org.fouryouandme.core.ext.startCoroutineAsync
import org.fouryouandme.researchkit.recorder.RecorderService
import org.fouryouandme.researchkit.recorder.RecorderServiceConnection
import org.fouryouandme.researchkit.recorder.RecordingState
import org.fouryouandme.researchkit.recorder.SensorData
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.tasks.TaskStateUpdate
import java.io.File

class SensorStepFragment : StepFragment(R.layout.step_sensor) {

    private val serviceConnection: RecorderServiceConnection =
        RecorderServiceConnection(
            { binder ->

                val step =
                    viewModel.getStepByIndexAs<SensorStep>(indexArg())

                step?.let {

                    startCoroutineAsync {
                        binder.bind(getOutputDirectory(), step, viewModel.state().task)
                    }

                    binder.stateLiveData()
                        .observeEvent(SensorStepFragment::class.java.simpleName) { state ->

                            when (state) {
                                is RecordingState.ResultCollected ->
                                    if (state.stepIdentifier == step.identifier)
                                        startCoroutineAsync {
                                            state.files.forEach { viewModel.addResult(it) }
                                        }
                                is RecordingState.Completed ->
                                    if (state.stepIdentifier == step.identifier)
                                        startCoroutineAsync { next() }
                                is RecordingState.Failure ->
                                    if (state.stepIdentifier == step.identifier)
                                        Toast.makeText(
                                            requireContext(),
                                            "Fallito",
                                            Toast.LENGTH_LONG
                                        ).show()
                            }

                        }

                    binder.sensorLiveData()
                        .observeEvent(SensorStepFragment::class.java.simpleName) {

                            when (it) {
                                is SensorData.Steps -> infoToast("steps: ${it.steps}")
                            }

                        }

                }

                viewModel.stateLiveData()
                    .observeEvent(SensorStepFragment::class.java.simpleName) {
                        when (it) {
                            is TaskStateUpdate.Cancelled ->
                                if (it.isCancelled) startCoroutineAsync { binder.stop() }
                        }
                    }
            },
            {}
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<SensorStep>(indexArg())

        step?.let { applyData(it) }
    }

    fun applyData(
        step: SensorStep
    ): Unit {

        clearFolder()

        root.setBackgroundColor(step.backgroundColor)

        title.text = step.title
        title.setTextColor(step.titleColor)

        description.text = step.description
        description.setTextColor(step.descriptionColor)

        RecorderService.start(requireContext().applicationContext, serviceConnection)

    }

    private fun clearFolder(): Unit {

        val dir = getOutputDirectory()

        if (dir.exists())
            dir.deleteRecursively()

    }

    /**
     * @return directory for outputting data logger files
     */
    private fun getOutputDirectory(): File =
        File("${requireContext().applicationContext.filesDir.absolutePath}/sensors")

    override fun onDestroy() {

        unbindServiceIO(serviceConnection)

        super.onDestroy()

    }
}