package com.foryouandme.core.researchkit.task

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.nineholepeg.NineHolePegTask

class FYAMNineHolePegTask(
    id: String,
    private val configuration: Configuration
) : Task(TaskIdentifiers.NINE_HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val secondary =
            configuration.theme.secondaryColor.color()

        NineHolePegTask.getNineHolePegCoreSteps(
            nineHolePegBackgroundColor = secondary,
            nineHolePegTitle = null,
            nineHolePegTitleColor = primaryText,
            nineHolePegDescriptionShape = null,
            nineHolePegDescriptionGrab = null,
            nineHolePegDescriptionRelease = null,
            nineHolePegDescriptionColor = primaryText
        )

    }

}