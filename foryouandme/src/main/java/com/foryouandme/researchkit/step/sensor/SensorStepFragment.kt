package com.foryouandme.researchkit.step.sensor

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.foryouandme.R
import com.foryouandme.core.ext.infoToast
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.researchkit.recorder.RecorderService
import com.foryouandme.researchkit.recorder.RecorderServiceConnection
import com.foryouandme.researchkit.recorder.RecordingState
import com.foryouandme.researchkit.recorder.SensorData
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.ui.tasks.TaskStateUpdate
import kotlinx.android.synthetic.main.step_sensor.*
import kotlin.math.roundToInt

class SensorStepFragment : StepFragment(R.layout.step_sensor) {

    private val serviceConnection: RecorderServiceConnection =
        RecorderServiceConnection(
            { binder ->

                val step =
                    viewModel.getStepByIndexAs<SensorStep>(indexArg())

                step?.let {

                    startCoroutineAsync {
                        binder.bind(
                            taskFragment().getSensorOutputDirectory(),
                            step,
                            viewModel.state().task
                        )
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

        root.setBackgroundColor(step.backgroundColor)

        if (step.image != null) {
            image.visibility = View.VISIBLE
            val lp = image.layoutParams
            val displayMetrics = DisplayMetrics()

            requireActivity().windowManager
                .defaultDisplay
                .getMetrics(displayMetrics)

            val height = displayMetrics.heightPixels
            lp.height = (height * 0.4).roundToInt()
            image.layoutParams = lp
            image.setImageResource(step.image)
            image.setBackgroundColor(Color.argb(255, 227, 227, 227))
        } else {
            image.visibility = View.GONE
        }

        title.text = step.title(requireContext())
        title.setTextColor(step.titleColor)

        description.text = step.description(requireContext())
        description.setTextColor(step.descriptionColor)

        RecorderService.start(requireContext().applicationContext, serviceConnection)

    }

    override fun onDestroy() {

        unbindService(serviceConnection)

        super.onDestroy()

    }
}