package org.fouryouandme.core.researchkit.task

import arrow.syntax.function.pipe
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.researchkit.step.FYAMPageStep
import org.fouryouandme.core.view.page.EPageType
import org.fouryouandme.researchkit.step.Back
import org.fouryouandme.researchkit.step.Step
import org.fouryouandme.researchkit.task.Task
import org.fouryouandme.researchkit.task.TaskIdentifiers
import org.fouryouandme.researchkit.task.gait.GaitTask

class FYAMGaitTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val moshi: Moshi
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        val secondary =
            configuration.theme.secondaryColor.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        welcomePage.asList().mapIndexed { index, page ->
            FYAMPageStep(
                getSurveyWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO
            )
        }.plus(
            GaitTask.getGaitCoreSteps(
                startBackImage = imageConfiguration.backSecondary(),
                startBackgroundColor = secondary,
                startTitle = null,
                startTitleColor = primaryText,
                startDescription = null,
                startDescriptionColor = primaryText,
                startButton = null,
                startButtonColor = primaryEnd,
                startButtonTextColor = secondary,
                introBackImage = imageConfiguration.backSecondary(),
                introBackgroundColor = secondary,
                introTitle = null,
                introTitleColor = primaryText,
                introDescription = null,
                introDescriptionColor = primaryText,
                introImage = imageConfiguration.pocket(),
                introButton = null,
                introButtonColor = primaryEnd,
                introButtonTextColor = secondary,
                countDownBackImage = imageConfiguration.backSecondary(),
                countDownBackgroundColor = secondary,
                countDownTitle = null,
                countDownTitleColor = primaryText,
                countDownDescription = null,
                countDownDescriptionColor = primaryText,
                countDownSeconds = 5,
                countDownCounterColor = primaryText,
                countDownCounterProgressColor = primaryEnd,
                outboundBackgroundColor = secondary,
                outboundTitle = null,
                outboundTitleColor = primaryText,
                outboundDescription = null,
                outboundDescriptionColor = primaryText,
                returnBackgroundColor = secondary,
                returnTitle = null,
                returnTitleColor = primaryText,
                returnDescription = null,
                returnDescriptionColor = primaryText,
                restBackgroundColor = secondary,
                restTitle = null,
                restTitleColor = primaryText,
                restDescription = null,
                restDescriptionColor = primaryText,
                moshi = moshi
            ).pipe { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getSurveySuccessStepId(it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS
                        )
                    )

                } ?: list

            }
        )

    }

    private fun getSurveyWelcomeStepId(introId: String): String =
        "${GaitTask.GAIT_WELCOME}_${introId}"

    private fun getSurveySuccessStepId(successId: String): String =
        "${GaitTask.GAIT_END}_${successId}"

}