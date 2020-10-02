package org.fouryouandme.core.arch.deps.task

import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.step.chooseOne.ChooseOneAnswer
import org.fouryouandme.researchkit.step.chooseOne.ChooseOneStep
import org.fouryouandme.researchkit.step.picker.PickerStep
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.utils.ImageResource.AndroidResource.Companion.toAndroidResource

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
                    imageConfiguration.videoDiaryIntro().toAndroidResource(),
                    "1",
                    { "Select a number" },
                    configuration.theme.primaryTextColor.color(),
                    configuration.theme.primaryTextColor.color(),
                    imageConfiguration.signUpNextStepSecondary().toAndroidResource()
                ),
                ChooseOneStep(
                    "choose one",
                    listOf(
                        ChooseOneAnswer(
                            "1",
                            "Answer 1",
                            configuration.theme.primaryTextColor.color(),
                            configuration.theme.primaryColorEnd.color()
                        ),
                        ChooseOneAnswer(
                            "2",
                            "Answer 2",
                            configuration.theme.primaryTextColor.color(),
                            configuration.theme.primaryColorEnd.color()
                        ),
                        ChooseOneAnswer(
                            "3",
                            "Answer 3",
                            configuration.theme.primaryTextColor.color(),
                            configuration.theme.primaryColorEnd.color()
                        ),
                        ChooseOneAnswer(
                            "4",
                            "Answer 4",
                            configuration.theme.primaryTextColor.color(),
                            configuration.theme.primaryColorEnd.color()
                        )
                    ),
                    configuration.theme.secondaryColor.color(),
                    imageConfiguration.videoDiaryIntro().toAndroidResource(),
                    "1",
                    { "Select an answer" },
                    configuration.theme.primaryTextColor.color(),
                    configuration.theme.primaryTextColor.color(),
                    imageConfiguration.signUpNextStepSecondary().toAndroidResource()
                )
            )


    }