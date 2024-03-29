package com.foryouandme.researchkit.task.trailmaking

import com.foryouandme.R
import com.foryouandme.core.ext.toTextSource
import com.foryouandme.researchkit.step.Back
import com.foryouandme.researchkit.step.Block
import com.foryouandme.researchkit.step.Step
import com.foryouandme.researchkit.step.beforestart.WelcomeStep
import com.foryouandme.researchkit.step.countdown.CountDownStep
import com.foryouandme.researchkit.step.end.EndStep
import com.foryouandme.researchkit.step.introduction.IntroductionStep
import com.foryouandme.researchkit.step.trailmaking.ETrailMakingType
import com.foryouandme.researchkit.step.trailmaking.TrailMakingStep
import com.foryouandme.researchkit.task.Task
import com.foryouandme.researchkit.task.TaskIdentifiers

class TrailMakingTask(
    id: String,
    stepColor: Int,
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
    secondaryIntroBackImage: Int,
    secondaryIntroBackgroundColor: Int,
    secondaryIntroTitle: String?,
    secondaryIntroTitleColor: Int,
    secondaryIntroDescription: String?,
    secondaryIntroDescriptionColor: Int,
    secondaryIntroImage: Int,
    secondaryIntroButton: String?,
    secondaryIntroButtonColor: Int,
    secondaryIntroButtonTextColor: Int,
    countDownBackImage: Int,
    countDownBackgroundColor: Int,
    countDownTitle: String?,
    countDownTitleColor: Int,
    countDownDescription: String?,
    countDownDescriptionColor: Int,
    countDownSeconds: Int,
    countDownCounterColor: Int,
    countDownCounterProgressColor: Int,
    trailMakingType: ETrailMakingType,
    trailMakingBackgroundColor: Int,
    trailMakingTimerAndErrorTextColor: Int,
    trailMakingTitleText: String,
    trailMakingTitleTextColor: Int,
    trailMakingPointColor: Int,
    trailMakingPointErrorColor: Int,
    trailMakingPointTextColor: Int,
    trailMakingLineTextColor: Int,
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
) : Task(TaskIdentifiers.TRAIL_MAKING, id) {

    override val steps: List<Step> by lazy {

        listOf(

            WelcomeStep(
                identifier = TRAIL_MAKING_WELCOME,
                back = Back(welcomeBackImage),
                backgroundColor = welcomeBackgroundColor,
                image = welcomeImage,
                title = {
                    welcomeTitle ?: it.getString(R.string.TRAIL_MAKING_welcome_title)
                },
                titleColor = welcomeTitleColor,
                description = {
                    welcomeDescription
                        ?: it.getString(R.string.TRAIL_MAKING_welcome_description)
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
            getTrailMakingCoreSteps(
                stepColor = stepColor,
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
                secondaryIntroBackImage = secondaryIntroBackImage,
                secondaryIntroBackgroundColor = secondaryIntroBackgroundColor,
                secondaryIntroTitle = secondaryIntroTitle,
                secondaryIntroTitleColor = secondaryIntroTitleColor,
                secondaryIntroDescription = secondaryIntroDescription,
                secondaryIntroDescriptionColor = secondaryIntroDescriptionColor,
                secondaryIntroImage = secondaryIntroImage,
                secondaryIntroButton = secondaryIntroButton,
                secondaryIntroButtonColor = secondaryIntroButtonColor,
                secondaryIntroButtonTextColor = secondaryIntroButtonTextColor,
                countDownBackImage = countDownBackImage,
                countDownBackgroundColor = countDownBackgroundColor,
                countDownTitle = countDownTitle,
                countDownTitleColor = countDownTitleColor,
                countDownDescription = countDownDescription,
                countDownDescriptionColor = countDownDescriptionColor,
                countDownSeconds = countDownSeconds,
                countDownCounterColor = countDownCounterColor,
                countDownCounterProgressColor = countDownCounterProgressColor,
                trailMakingType = trailMakingType,
                trailMakingBackgroundColor = trailMakingBackgroundColor,
                trailMakingTimerAndErrorTextColor = trailMakingTimerAndErrorTextColor,
                trailMakingTitleText = trailMakingTitleText,
                trailMakingTitleTextColor = trailMakingTitleTextColor,
                trailMakingPointColor = trailMakingPointColor,
                trailMakingPointErrorColor = trailMakingPointErrorColor,
                trailMakingPointTextColor = trailMakingPointTextColor,
                trailMakingLineTextColor = trailMakingLineTextColor
            )
        ).plus(

            EndStep(
                identifier = TRAIL_MAKING_END,
                backgroundColor = endBackgroundColor,
                title = {
                    endTitle ?: it.getString(R.string.TRAIL_MAKING_end_title)
                },
                titleColor = endTitleColor,
                description = {
                    endDescription
                        ?: it.getString(R.string.TRAIL_MAKING_end_description)
                },
                descriptionColor = endDescriptionColor,
                button = {
                    endButton ?: it.getString(R.string.TRAIL_MAKING_button)
                },
                buttonColor = endButtonColor,
                buttonTextColor = endButtonTextColor,
                close = endClose,
                checkMarkBackgroundColor = endCheckMarkBackgroundColor,
                checkMarkColor = endCheckMarkColor
            )

        )

    }


    companion object {

        const val TRAIL_MAKING_BLOCK = "trail_making_block"

        const val TRAIL_MAKING: String = "trail_making"

        const val TRAIL_MAKING_WELCOME: String = "trail_making_welcome"

        const val TRAIL_MAKING_START: String = "trail_making_start"

        const val TRAIL_MAKING_INTRO: String = "trail_making_intro"

        const val TRAIL_MAKING_SECONDARY_INTRO: String = "trail_making_secondary_intro"

        const val TRAIL_MAKING_COUNT_DOWN: String = "trail_making_count_down"

        const val TRAIL_MAKING_END: String = "trail_making_end"

        fun getTrailMakingCoreSteps(
            stepColor: Int,
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
            secondaryIntroBackImage: Int,
            secondaryIntroBackgroundColor: Int,
            secondaryIntroTitle: String?,
            secondaryIntroTitleColor: Int,
            secondaryIntroDescription: String?,
            secondaryIntroDescriptionColor: Int,
            secondaryIntroImage: Int,
            secondaryIntroButton: String?,
            secondaryIntroButtonColor: Int,
            secondaryIntroButtonTextColor: Int,
            countDownBackImage: Int,
            countDownBackgroundColor: Int,
            countDownTitle: String?,
            countDownTitleColor: Int,
            countDownDescription: String?,
            countDownDescriptionColor: Int,
            countDownSeconds: Int,
            countDownCounterColor: Int,
            countDownCounterProgressColor: Int,
            trailMakingType: ETrailMakingType,
            trailMakingBackgroundColor: Int,
            trailMakingTimerAndErrorTextColor: Int,
            trailMakingTitleText: String?,
            trailMakingTitleTextColor: Int,
            trailMakingPointColor: Int,
            trailMakingPointErrorColor: Int,
            trailMakingPointTextColor: Int,
            trailMakingLineTextColor: Int,
        ): List<Step> =

            listOf(
                IntroductionStep(
                    identifier = TRAIL_MAKING_START,
                    block = Block(TRAIL_MAKING_BLOCK, stepColor),
                    back = Back(startBackImage),
                    backgroundColor = startBackgroundColor,
                    title = startTitle.toTextSource(R.string.TRAIL_MAKING_welcome_title),
                    titleColor = startTitleColor,
                    description =
                    startDescription.toTextSource(
                        R.string.TRAIL_MAKING_welcome_description
                    ),
                    descriptionColor = startDescriptionColor,
                    image = listOf(startImage),
                    button = startButton.toTextSource(R.string.TASK_next),
                    buttonColor = startButtonColor,
                    buttonTextColor = startButtonTextColor,
                ),
                IntroductionStep(
                    identifier = TRAIL_MAKING_INTRO,
                    block = Block(TRAIL_MAKING_BLOCK, stepColor),
                    back = Back(introBackImage),
                    backgroundColor = introBackgroundColor,
                    title =
                    introTitle.toTextSource(R.string.TRAIL_MAKING_welcome_title),
                    titleColor = introTitleColor,
                    description =
                    introDescription.toTextSource(
                        R.string.TRAIL_MAKING_intro
                    ),
                    descriptionColor = introDescriptionColor,
                    image = listOf(introImage),
                    button = introButton.toTextSource(R.string.TASK_next),
                    buttonColor = introButtonColor,
                    buttonTextColor = introButtonTextColor,
                ),
                IntroductionStep(
                    identifier = TRAIL_MAKING_SECONDARY_INTRO,
                    block = Block(TRAIL_MAKING_BLOCK, stepColor),
                    back = Back(secondaryIntroBackImage),
                    backgroundColor = secondaryIntroBackgroundColor,
                    title = secondaryIntroTitle.toTextSource(R.string.TRAIL_MAKING_welcome_title),
                    titleColor = secondaryIntroTitleColor,
                    description =
                    secondaryIntroDescription.toTextSource(
                        R.string.TRAIL_MAKING_secondary_intro
                    ),
                    descriptionColor = secondaryIntroDescriptionColor,
                    image = listOf(secondaryIntroImage),
                    button = secondaryIntroButton.toTextSource(R.string.TASK_get_started),
                    buttonColor = secondaryIntroButtonColor,
                    buttonTextColor = secondaryIntroButtonTextColor,
                ),
                CountDownStep(
                    identifier = TRAIL_MAKING_COUNT_DOWN,
                    block = Block(TRAIL_MAKING_BLOCK, stepColor),
                    back = Back(countDownBackImage),
                    backgroundColor = countDownBackgroundColor,
                    titleColor = countDownTitleColor,
                    title = {
                        countDownTitle
                            ?: it.getString(R.string.TRAIL_MAKING_welcome_title)
                    },
                    description = {
                        countDownDescription ?: it.getString(R.string.TASK_countdown)
                    },
                    descriptionColor = countDownDescriptionColor,
                    seconds = countDownSeconds,
                    counterColor = countDownCounterColor,
                    counterProgressColor = countDownCounterProgressColor
                ),
                TrailMakingStep(
                    TRAIL_MAKING,
                    Block(TRAIL_MAKING_BLOCK, stepColor),
                    trailMakingType,
                    trailMakingBackgroundColor,
                    trailMakingTimerAndErrorTextColor,
                    trailMakingTitleText,
                    trailMakingTitleTextColor,
                    trailMakingPointColor,
                    trailMakingPointErrorColor,
                    trailMakingPointTextColor,
                    trailMakingLineTextColor
                )
            )

    }

}