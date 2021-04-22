package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.holepeg.HolePegTask

class FYAMHolePegTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration
) : Task(TaskIdentifiers.HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        val primary =
            configuration.theme.primaryColorEnd.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val secondary =
            configuration.theme.secondaryColor.color()

        HolePegTask.getHolePegCoreSteps(
            introBackImage = imageConfiguration.backSecondary(),
            introBackgroundColor = secondary,
            introTitle = null,
            introTitleColor = primaryText,
            introDescription = null,
            introDescriptionColor = primaryText,
            introImage = null,
            introButton = null,
            introButtonColor = primary,
            introButtonTextColor = secondary,
            holePegBackgroundColor = secondary,
            holePegTitle = null,
            holePegTitleColor = primaryText,
            holePegDescriptionStartCenter = null,
            holePegDescriptionEndCenter = null,
            holePegDescriptionStart = null,
            holePegDescriptionEnd = null,
            holePegDescriptionGrab = null,
            holePegDescriptionRelease = null,
            holePegDescriptionColor = primaryText,
            holePegProgressColor = primary,
            holePegPointColor = primary,
            holePegTargetColor = primary
        )

    }

}