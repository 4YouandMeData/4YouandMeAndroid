package org.fouryouandme.core.arch.deps.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.picker.PickerStep
import org.fouryouandme.researchkit.task.Task

// TODO: handle dynamic task creation
fun buildSurvey(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration
): Task =
    object : Task("survey", id) {
        override val steps: List<Step> =
            listOf(
                PickerStep(
                    "number",
                    listOf("1", "2", "3", "4", "5", "More than 5"),
                    configuration.theme.secondaryColor.color(),
                    { "Select a number" },
                    configuration.theme.primaryTextColor.color(),
                    configuration.theme.primaryTextColor.color(),
                    imageConfiguration.signUpNextStepSecondary()
                )
            )


    }