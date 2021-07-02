package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.core.researchkit.step.page.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.gait.GaitTask
import com.squareup.moshi.Moshi

class FYAMGaitTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val pages: List<Page>,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val reschedule: Reschedule?,
    private val moshi: Moshi
) : Task(TaskIdentifiers.GAIT, id) {

    override val steps: List<Step> by lazy {

        val secondary =
            configuration.theme.secondaryColor.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                getGaitWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO,
                index == 0 && reschedule.isEnabled()
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
            ).let { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getGaitSuccessStepId(it.id),
                            Back(imageConfiguration.backSecondary()),
                            configuration,
                            it,
                            EPageType.SUCCESS,
                            false
                        )
                    )

                } ?: list

            }
        )

    }

    private fun getGaitWelcomeStepId(introId: String): String =
        "${GaitTask.GAIT_WELCOME}_${introId}"

    private fun getGaitSuccessStepId(successId: String): String =
        "${GaitTask.GAIT_END}_${successId}"

}