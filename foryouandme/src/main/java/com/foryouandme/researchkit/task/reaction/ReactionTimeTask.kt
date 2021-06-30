package com.foryouandme.researchkit.task.reaction

import com.foryouandme.R
import com.foryouandme.core.ext.toTextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.beforestart.WelcomeStep
import com.foryouandme.researchkit.step.end.EndStep
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.step.reaction.ReactionTimeStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers
import com.squareup.moshi.Moshi

class ReactionTimeTask(
        id: String,
        welcomeRemindMeLater: Boolean,
        welcomeBackImage: Int,
        welcomeBackgroundColor: Int,
        welcomeImage: Int,
        welcomeTitle: String?,
        welcomeTitleColor: Int,
        welcomeDescription: String?,
        welcomeDescriptionColor: Int,
        welcomeRemindButton: String?,
        welcomeRemindButtonColor: Int,
        welcomeRemindButtonTextColor: Int,
        welcomeStartButton: String?,
        welcomeStartButtonColor: Int,
        welcomeStartButtonTextColor: Int,
        welcomeShadowColor: Int,
        startBackImage: Int,
        startBackgroundColor: Int,
        startTitle: String?,
        startTitleColor: Int,
        startDescription: String?,
        startDescriptionColor: Int,
        startImage: Int,
        startButton: String?,
        startButtonColor: Int,
        startButtonTextColor: Int,
        introBackImage: Int,
        introBackgroundColor: Int,
        introTitle: String?,
        introTitleColor: Int,
        introDescription: String?,
        introDescriptionColor: Int,
        introImage: Int,
        introButton: String?,
        introButtonColor: Int,
        introButtonTextColor: Int,
        reactionTimeBackgroundColor: Int,
        reactionTimeTitle: String?,
        reactionTimeTitleColor: Int,
        reactionTimeAttemptColor: Int,
        reactionCheckMarkBackgroundColor: Int,
        reactionCheckMarkColor: Int,
        reactionTimeMaximumStimulusIntervalSeconds: Long,
        reactionTimeMinimumStimulusIntervalSeconds: Long,
        reactionTimeNumberOfAttempts: Int,
        reactionTimeTimeoutSeconds: Long,
        endBackgroundColor: Int,
        endTitle: String?,
        endTitleColor: Int,
        endDescription: String?,
        endDescriptionColor: Int,
        endButton: String?,
        endButtonColor: Int,
        endButtonTextColor: Int,
        endClose: Boolean = false,
        endCheckMarkBackgroundColor: Int,
        endCheckMarkColor: Int,
        private val moshi: Moshi
) : Task(TaskIdentifiers.REACTION_TIME, id) {

    override val steps: List<Step> by lazy {

        listOf(

                WelcomeStep(
                        identifier = REACTION_TIME_WELCOME,
                        back = Back((welcomeBackImage)),
                        backgroundColor = welcomeBackgroundColor,
                        image = welcomeImage,
                        title = {
                            welcomeTitle ?: it.getString(R.string.REACTION_TIME_welcome_title)
                        },
                        titleColor = welcomeTitleColor,
                        description = {
                            welcomeDescription
                                    ?: it.getString(R.string.REACTION_TIME_welcome_description)
                        },
                        descriptionColor = welcomeDescriptionColor,
                        remindButton = {
                            welcomeRemindButton ?: it.getString(R.string.TASK_welcome_remind_button)
                        },
                        remindButtonColor = welcomeRemindButtonColor,
                        remindButtonTextColor = welcomeRemindButtonTextColor,
                        startButton = {
                            welcomeStartButton ?: it.getString(R.string.TASK_welcome_start_button)
                        },
                        startButtonColor = welcomeStartButtonColor,
                        startButtonTextColor = welcomeStartButtonTextColor,
                        shadowColor = welcomeShadowColor,
                        remindMeLater = welcomeRemindMeLater
                )

        ).plus(

                getReactionTimeCoreSteps(
                        startBackImage = startBackImage,
                        startBackgroundColor = startBackgroundColor,
                        startTitle = startTitle,
                        startTitleColor = startTitleColor,
                        startDescription = startDescription,
                        startDescriptionColor = startDescriptionColor,
                        startImage = startImage,
                        startButton = startButton,
                        startButtonColor = startButtonColor,
                        startButtonTextColor = startButtonTextColor,
                        introBackImage = introBackImage,
                        introBackgroundColor = introBackgroundColor,
                        introTitle = introTitle,
                        introTitleColor = introTitleColor,
                        introDescription = introDescription,
                        introDescriptionColor = introDescriptionColor,
                        introImage = introImage,
                        introButton = introButton,
                        introButtonColor = introButtonColor,
                        introButtonTextColor = introButtonTextColor,
                        reactionTimeBackgroundColor = reactionTimeBackgroundColor,
                        reactionTimeTitle = reactionTimeTitle,
                        reactionTimeTitleColor = reactionTimeTitleColor,
                        reactionTimeAttemptColor = reactionTimeAttemptColor,
                        reactionCheckMarkBackgroundColor = reactionCheckMarkBackgroundColor,
                        reactionCheckMarkColor = reactionCheckMarkColor,
                        reactionTimeMaximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
                        reactionTimeMinimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
                        reactionTimeNumberOfAttempts = reactionTimeNumberOfAttempts,
                        reactionTimeTimeoutSeconds = reactionTimeTimeoutSeconds
                )

        ).plus(

                EndStep(
                        identifier = REACTION_TIME_END,
                        backgroundColor = endBackgroundColor,
                        title = { endTitle ?: it.getString(R.string.REACTION_TIME_end_title) },
                        titleColor = endTitleColor,
                        description = { endDescription ?: it.getString(R.string.REACTION_TIM_end_description) },
                        descriptionColor = endDescriptionColor,
                        button = { endButton ?: it.getString(R.string.REACTION_TIME_button) },
                        buttonColor = endButtonColor,
                        buttonTextColor = endButtonTextColor,
                        close = endClose,
                        checkMarkBackgroundColor = endCheckMarkBackgroundColor,
                        checkMarkColor = endCheckMarkColor
                )

        )

    }


    companion object {

        const val REACTION_TIME = "reaction_time"

        const val REACTION_TIME_WELCOME = "reaction_time_welcome"

        const val REACTION_TIME_START = "reaction_time_start"

        const val REACTION_TIME_INTRO = "reaction_time_intro"

        const val REACTION_TIME_END = "reaction_time_end"

        fun getReactionTimeCoreSteps(
                startBackImage: Int,
                startBackgroundColor: Int,
                startTitle: String?,
                startTitleColor: Int,
                startDescription: String?,
                startDescriptionColor: Int,
                startImage: Int,
                startButton: String?,
                startButtonColor: Int,
                startButtonTextColor: Int,
                introBackImage: Int,
                introBackgroundColor: Int,
                introTitle: String?,
                introTitleColor: Int,
                introDescription: String?,
                introDescriptionColor: Int,
                introImage: Int,
                introButton: String?,
                introButtonColor: Int,
                introButtonTextColor: Int,
                reactionTimeBackgroundColor: Int,
                reactionTimeTitle: String?,
                reactionTimeTitleColor: Int,
                reactionTimeAttemptColor: Int,
                reactionCheckMarkBackgroundColor: Int,
                reactionCheckMarkColor: Int,
                reactionTimeMaximumStimulusIntervalSeconds: Long,
                reactionTimeMinimumStimulusIntervalSeconds: Long,
                reactionTimeNumberOfAttempts: Int,
                reactionTimeTimeoutSeconds: Long
        ): List<Step> =

                listOf(
                        IntroductionStep(
                                identifier = REACTION_TIME_START,
                                back = Back(startBackImage),
                                backgroundColor = startBackgroundColor,
                                title =
                                    startTitle.toTextSource(R.string.REACTION_TIME_welcome_title),
                                titleColor = startTitleColor,
                                description =
                                    startDescription.toTextSource(
                                            R.string.REACTION_TIME_start
                                    ),
                                descriptionColor = startDescriptionColor,
                                image = listOf(startImage),
                                button = startButton.toTextSource(R.string.TASK_next),
                                buttonColor = startButtonColor,
                                buttonTextColor = startButtonTextColor
                        ),
                        IntroductionStep(
                                identifier = REACTION_TIME_INTRO,
                                back = Back(introBackImage),
                                backgroundColor = introBackgroundColor,
                                title = introTitle.toTextSource(R.string.REACTION_TIME_welcome_title),
                                titleColor = introTitleColor,
                                description =
                                    introDescription.toTextSource(
                                            R.string.REACTION_TIME_intro,
                                            "3"
                                    ),
                                descriptionColor = introDescriptionColor,
                                image = listOf(introImage),
                                button = introButton.toTextSource(R.string.TASK_get_started),
                                buttonColor = introButtonColor,
                                buttonTextColor = introButtonTextColor,
                        ),
                        ReactionTimeStep(
                                REACTION_TIME,
                                backgroundColor = reactionTimeBackgroundColor,
                                titleText = reactionTimeTitle,
                                titleTextColor = reactionTimeTitleColor,
                                attemptsTextColor = reactionTimeAttemptColor,
                                checkMarkBackgroundColor = reactionCheckMarkBackgroundColor,
                                checkMarkColor = reactionCheckMarkColor,
                                maximumStimulusIntervalSeconds = reactionTimeMaximumStimulusIntervalSeconds,
                                minimumStimulusIntervalSeconds = reactionTimeMinimumStimulusIntervalSeconds,
                                numberOfAttempts = reactionTimeNumberOfAttempts,
                                timeoutSeconds = reactionTimeTimeoutSeconds
                        )
                )

    }

}