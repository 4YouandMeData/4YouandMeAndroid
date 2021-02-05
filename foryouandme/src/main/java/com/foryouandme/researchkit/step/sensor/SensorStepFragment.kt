package com.foryouandme.researchkit.step.sensor

import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.foryouandme.R
import com.foryouandme.core.ext.infoToast
import com.foryouandme.core.ext.isTerminated
import com.foryouandme.core.ext.launchSafe
import com.foryouandme.core.ext.startCoroutineAsync
import com.foryouandme.databinding.StepSensorBinding
import com.foryouandme.researchkit.recorder.RecorderService
import com.foryouandme.researchkit.recorder.RecorderServiceConnection
import com.foryouandme.researchkit.recorder.RecordingState
import com.foryouandme.researchkit.recorder.SensorData
import com.foryouandme.researchkit.step.StepFragment
import com.foryouandme.ui.tasks.TaskStateUpdate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@AndroidEntryPoint
class SensorStepFragment : StepFragment(R.layout.step_sensor) {

    private val binding: StepSensorBinding?
        get() = view?.let { StepSensorBinding.bind(it) }

    private var timerJob: Job? = null

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

        viewModel.getStepByIndexAs<SensorStep>(indexArg())?.let { applyData(it) }

    }

    fun applyData(step: SensorStep) {

        val viewBinding = binding

        if (viewBinding != null) {

            viewBinding.root.setBackgroundColor(step.backgroundColor)

            if (step.image != null) {
                viewBinding.image.visibility = View.VISIBLE
                val lp = viewBinding.image.layoutParams
                val displayMetrics = DisplayMetrics()

                requireActivity().windowManager
                    .defaultDisplay
                    .getMetrics(displayMetrics)

                val height = displayMetrics.heightPixels
                lp.height = (height * 0.4).roundToInt()
                viewBinding.image.layoutParams = lp
                viewBinding.image.setImageResource(step.image)
                viewBinding.image.setBackgroundColor(Color.argb(255, 227, 227, 227))
            } else {
                viewBinding.image.visibility = View.GONE
            }

            viewBinding.title.text = step.title(requireContext())
            viewBinding.title.setTextColor(step.titleColor)

            viewBinding.description.text = step.description(requireContext())
            viewBinding.description.setTextColor(step.descriptionColor)

            RecorderService.start(requireContext().applicationContext, serviceConnection)

            viewBinding.timer.isVisible = step.timerSeconds != null
            viewBinding.timer.setTextColor(step.descriptionColor)

            if (
                (timerJob == null || timerJob.isTerminated) &&
                step.timerSeconds != null &&
                step.timerSeconds > 0
            )
                timerJob = lifecycleScope.launchSafe {

                    var seconds = step.timerSeconds

                    while (seconds > 0) {

                        val minuteValue = (seconds / 60).toString().formatTimeWithZero()
                        val secondsValue = (seconds % 60).toString().formatTimeWithZero()

                        val timer = "$minuteValue:$secondsValue"
                        binding?.timer?.text = timer
                        seconds--
                        delay(1000)

                    }

                }

        }

    }

    private fun String.formatTimeWithZero(): String =
        if (length == 1) "0$this" else this

    override fun onDestroy() {

        unbindService(serviceConnection)

        super.onDestroy()

    }

}