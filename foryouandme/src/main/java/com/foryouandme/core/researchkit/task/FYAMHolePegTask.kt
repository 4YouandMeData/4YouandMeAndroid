package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.researchkit.step.page.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.holepeg.HolePegTask

class FYAMHolePegTask(
    id: String,
    private val configuration: Configuration,
    private val imageConfiguration: ImageConfiguration,
    private val pages: List<Page>,
    private val welcomePage: Page,
    private val successPage: Page?,
    private val reschedule: Reschedule?
) : Task(TaskIdentifiers.HOLE_PEG, id) {

    override val steps: List<Step> by lazy {

        val primary =
            configuration.theme.primaryColorEnd.color()

        val primaryText =
            configuration.theme.primaryTextColor.color()

        val secondary =
            configuration.theme.secondaryColor.color()

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                getHolePegWelcomeStepId(page.id),
                Back(imageConfiguration.backSecondary()),
                configuration,
                page,
                EPageType.INFO,
                index == 0 && reschedule.isEnabled()
            )

        }.plus(
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
                tutorialBackImage = imageConfiguration.backSecondary(),
                tutorialBackgroundColor = secondary,
                tutorialTitle = null,
                tutorialTitleColor = primaryText,
                tutorialDescription = null,
                tutorialDescriptionColor = primaryText,
                tutorialImage = null,
                tutorialButton = null,
                tutorialButtonColor = primary,
                tutorialButtonTextColor = secondary,
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
            ).let { list ->

                successPage?.let {

                    list.plus(
                        FYAMPageStep(
                            getHolePegSuccessStepId(it.id),
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

    private fun getHolePegWelcomeStepId(introId: String): String =
        "hole_peg_welcome_${introId}"

    private fun getHolePegSuccessStepId(successId: String): String =
        "hole_peg_end_${successId}"

}