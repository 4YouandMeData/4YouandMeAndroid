package org.fouryouandme.researchkit.step.active

import android.os.Bundle
import android.view.View
import arrow.core.*
import arrow.core.extensions.fx
import kotlinx.android.synthetic.main.step_active.*
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.RecorderService
import org.fouryouandme.researchkit.recorder.RecorderServiceConnection
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.StepFragment
import org.fouryouandme.researchkit.task.Task
import java.io.File

class ActiveStepFragment : StepFragment(R.layout.step_active) {

    private var data: Option<Tuple2<Step.ActiveStep, Task>> = None
    private val stepDataObservable: StepDataObservable = StepDataObservable()

    private val serviceConnection: RecorderServiceConnection =
        RecorderServiceConnection(
            { binder ->

                data.map { (step, task) ->

                    binder.bind(getOutputDirectory(), step, task)
                }

                stepDataObservable.observe(StepDataObserver { (step, task) ->
                    binder.bind(getOutputDirectory(), step, task)
                })

                binder.stateLiveData().value

            },
            {}
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val step =
            viewModel.getStepByIndexAs<Step.ActiveStep>(indexArg())

        Option.fx { !step toT !viewModel.state().task }.map { applyData(it.a, it.b) }
    }

    fun applyData(
        step: Step.ActiveStep,
        task: Task
    ): Unit {

        root.setBackgroundColor(step.configuration.theme.secondaryColor.color())

        title.text = step.title
        title.setTextColor(step.configuration.theme.primaryTextColor.color())

        description.text = step.description
        description.setTextColor(step.configuration.theme.primaryTextColor.color())

        val tuple = step toT task

        data = tuple.some()
        stepDataObservable.notify(tuple)

        RecorderService.start(requireContext().applicationContext, serviceConnection)

    }

    override fun onDestroyView() {

        stepDataObservable.clear()

        super.onDestroyView()
    }

    /**
     * @return directory for outputting data logger files
     */
    private fun getOutputDirectory(): File = requireContext().applicationContext.filesDir

}