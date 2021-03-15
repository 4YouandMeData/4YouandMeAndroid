package com.foryouandme.core.researchkit.task

import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.researchkit.step.FYAMPageStep
import com.foryouandme.core.view.page.EPageType
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.page.Page
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.foryouandme.researchkit.task.reaction.ReactionTimeTask

class FYAMReactionTimeTask(
        id: String,
        reactionTimeTitle: String?,
        reactionTimeMaximumStimulusIntervalSeconds: Long,
        reactionTimeMinimumStimulusIntervalSeconds: Long,
        reactionTimeNumberOfAttempts: Int,
        reactionTimeTimeoutSeconds: Long,
        private val configuration: Configuration,
        private val imageConfiguration: ImageConfiguration,
        private val pages: List<Page>,
        private val welcomePage: Page,
        private val successPage: Page?,
        private val reschedule: Reschedule?
) : Task(TaskIdentifiers.REACTION_TIME, id) {

    override val steps: List<Step> by lazy {

        val primaryEnd =
                configuration.theme.primaryColorEnd.color()

        val primaryText =
                configuration.theme.primaryTextColor.color()

        val secondary =
                configuration.theme.secondaryColor.color()

        welcomePage.asList(pages).mapIndexed { index, page ->

            FYAMPageStep(
                    getReactionTimeWelcomeStepId(page.id),
                    Back(imageConfiguration.backSecondary()),
                    configuration,
                    page,
                    EPageType.INFO,
                    index == 0 && reschedule.isEnabled()
            )

        }.plus(
                ReactionTimeTask.getReactionTimeCoreSteps(
                        startBackImage = imageConfiguration.backSecondary(),
                        startBackgroundColor = secondary,
                        startTitle = null,
                        startTitleColor = primaryText,
                        startDescription = null,
                        startDescriptionColor = primaryText,
                        startImage = imageConfiguration.phoneShake(),
                        startButton = null,
                        startButtonColor = primaryEnd,
                        startButtonTextColor = secondary,
                        introBackImage = imageConfiguration.backSecondary(),
                        introBackgroundColor = secondary,
                        introTitle = null,
                        introTitleColor = primaryText,
                        introDescription = null,
                        introDescriptionColor = primaryText,
                        introImage = imageConfiguration.phoneShakeCircle(),
                        introButton = null,
                        introButtonColor = primaryEnd,
                        introButtonTextColor = secondary,
                        reactionTimeBackgroundColor = secondary,
                        reactionTimeTitle = reactionTimeTitle,
                        reactionTimeTitleColor = primaryText,
                        reactionTimeAttemptColor = primaryText,
                        reactionCheckMarkBackgroundColor = primaryEnd,
                        reactionCheckMarkColor = secondary,
                        reactionTimeMaximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
                        reactionTimeMinimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
                        reactionTimeNumberOfAttempts = reactionTimeNumberOfAttempts,
                        reactionTimeTimeoutSeconds = reactionTimeTimeoutSeconds
                ).pipe { list ->

                    successPage?.let {

                        list.plus(
                                FYAMPageStep(
                                        getReactionTimeSuccessStepId(it.id),
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

    private fun getReactionTimeWelcomeStepId(introId: String): String =
            "${ReactionTimeTask.REACTION_TIME_WELCOME}_${introId}"

    private fun getReactionTimeSuccessStepId(successId: String): String =
            "${ReactionTimeTask.REACTION_TIME_END}_${successId}"

}