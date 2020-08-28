package org.fouryouandme.researchkit.step.active

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import arrow.core.*
import kotlinx.android.synthetic.main.step_active.view.*
import org.fouryouandme.R
import org.fouryouandme.researchkit.recorder.RecorderService
import org.fouryouandme.researchkit.recorder.RecorderServiceConnection
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import java.io.File

class ActiveStepView(context: Context) : FrameLayout(context) {

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

            },
            {}
        )

    init {

        View.inflate(context, R.layout.step_active, this)

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

        RecorderService.start(context, serviceConnection)

    }

    override fun onDetachedFromWindow() {

        stepDataObservable.clear()

        super.onDetachedFromWindow()

    }

    /**
     * @return directory for outputting data logger files
     */
    private fun getOutputDirectory(): File = context.applicationContext.filesDir

}