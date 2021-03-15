package com.foryouandme.core.researchkit.task

import android.graphics.Color
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.trailmaking.ETrailMakingType
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.trailmaking.TrailMakingTask

class FYAMTrailMakingTask(
        id: String,
        private val title: String?,
        private val trailMakingType: ETrailMakingType,
        private val configuration: Configuration,
        private val imageConfiguration: ImageConfiguration,
        private val pages: List<Page>,
        private val welcomePage: Page,
        private val successPage: Page?,
        private val reschedule: Reschedule?
) : Task(TaskIdentifiers.TRAIL_MAKING, id) {

    override val steps: List<Step> by lazy {

        val primaryEnd =
                configuration.theme.primaryColorEnd.color()

        val primaryText =
                configuration.theme.primaryTextColor.color()

        val secondary =
                configuration.theme.secondaryColor.color()

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                    getTrailMakingTestWelcomeStepId(page.id),
                    Back(imageConfiguration.backSecondary()),
                    configuration,
                    page,
                    EPageType.INFO,
                    index == 0 && reschedule.isEnabled()
            )

        }.plus(

                TrailMakingTask.getTrailMakingCoreSteps(
                        startBackImage = imageConfiguration.backSecondary(),
                        startBackgroundColor = secondary,
                        startTitle = null,
                        startTitleColor = primaryText,
                        startDescription = null,
                        startDescriptionColor = primaryText,
                        startImage = imageConfiguration.trailMaking(),
                        startButton = null,
                        startButtonColor = primaryEnd,
                        startButtonTextColor = secondary,
                        introBackImage = imageConfiguration.backSecondary(),
                        introBackgroundColor = secondary,
                        introTitle = null,
                        introTitleColor = primaryText,
                        introDescription = null,
                        introDescriptionColor = primaryText,
                        introImage = imageConfiguration.trailMaking(),
                        introButton = null,
                        introButtonColor = primaryEnd,
                        introButtonTextColor = secondary,
                        secondaryIntroBackImage = imageConfiguration.backSecondary(),
                        secondaryIntroBackgroundColor = secondary,
                        secondaryIntroTitle = null,
                        secondaryIntroTitleColor = primaryText,
                        secondaryIntroDescription = null,
                        secondaryIntroDescriptionColor = primaryText,
                        secondaryIntroImage = imageConfiguration.trailMaking(),
                        secondaryIntroButton = null,
                        secondaryIntroButtonColor = primaryEnd,
                        secondaryIntroButtonTextColor = secondary,
                        countDownBackImage = imageConfiguration.backSecondary(),
                        countDownBackgroundColor = secondary,
                        countDownTitle = null,
                        countDownTitleColor = primaryText,
                        countDownDescription = null,
                        countDownDescriptionColor = primaryText,
                        countDownSeconds = 5,
                        countDownCounterColor = primaryEnd,
                        countDownCounterProgressColor = secondary,
                        trailMakingType = trailMakingType,
                        trailMakingBackgroundColor = secondary,
                        trailMakingTimerAndErrorTextColor = primaryText,
                        trailMakingTitleText = title,
                        trailMakingTitleTextColor = primaryText,
                        trailMakingPointColor = primaryEnd,
                        trailMakingPointErrorColor = Color.RED,
                        trailMakingPointTextColor = secondary,
                        trailMakingLineTextColor = primaryEnd
                )
        )

    }

    private fun getTrailMakingTestWelcomeStepId(introId: String): String =
            "${TrailMakingTask.TRAIL_MAKING_WELCOME}_${introId}"

    private fun getTrailMakingTestSuccessStepId(successId: String): String =
            "${TrailMakingTask.TRAIL_MAKING_END}_${successId}"
}