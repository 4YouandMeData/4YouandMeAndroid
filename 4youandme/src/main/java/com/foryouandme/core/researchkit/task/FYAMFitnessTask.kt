package com.foryouandme.core.researchkit.task

import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.page.Page
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.fitness.FitnessTask
import com.squareup.moshi.Moshi

class FYAMFitnessTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val moshi: Moshi
) : Task(TaskIdentifiers.FITNESS, id) {

    override val steps: List<Step> by lazy {

        val secondary =
            configuration.theme.secondaryColor.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val primaryEnd =
            configuration.theme.primaryColorEnd.color()

        welcomePage.asList().mapIndexed { index, page ->
            FYAMPageStep(
                getFitnessWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO
            )
        }.plus(
            FitnessTask.getFitnessCoreSteps(
                startBackImage = imageConfiguration.backSecondary(),
                startBackgroundColor = secondary,
                startTitle = null,
                startTitleColor = primaryText,
                startDescription = null,
                startDescriptionColor = primaryText,
                startImage = imageConfiguration.heartBeat(),
                startButton = null,
                startButtonColor = primaryEnd,
                startButtonTextColor = secondary,
                introBackImage = imageConfiguration.backSecondary(),
                introBackgroundColor = secondary,
                introTitle = null,
                introTitleColor = primaryText,
                introDescription = null,
                introDescriptionColor = primaryText,
                introImage = imageConfiguration.walkingMan(),
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
                countDownCounterColor = primaryEnd,
                countDownCounterProgressColor = secondary,
                walkBackgroundColor = secondary,
                walkTitle = null,
                walkTitleColor = primaryText,
                walkDescription = null,
                walkDescriptionColor = primaryText,
                walkImage = imageConfiguration.walkingMan(),
                sitBackgroundColor = secondary,
                sitTitle = null,
                sitTitleColor = primaryText,
                sitDescription = null,
                sitDescriptionColor = primaryText,
                sitImage = imageConfiguration.sittingMan(),
                moshi = moshi
            ).pipe { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getFitnessSuccessStepId(it.id),
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

    private fun getFitnessWelcomeStepId(introId: String): String =
        "${FitnessTask.FITNESS_WELCOME}_${introId}"

    private fun getFitnessSuccessStepId(successId: String): String =
        "${FitnessTask.FITNESS_END}_${successId}"

}