package com.foryouandme.core.researchkit.task

import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.researchkit.task.gait.GaitTask
import com.squareup.moshi.Moshi

suspend fun FYAMTaskConfiguration.buildGait(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    reschedule: Reschedule?,
    moshi: Moshi
): GaitTask {

    val secondary =
        configuration.theme.secondaryColor.color()

    val primaryText =
        configuration.theme.primaryTextColor.color()

    val primaryEnd =
        configuration.theme.primaryColorEnd.color()

    return GaitTask(
        id = id,
        welcomeBackImage = imageConfiguration.backSecondary(),
        welcomeBackgroundColor = secondary,
        welcomeImage = imageConfiguration.videoDiaryIntro(),
        welcomeTitle = configuration.text.gaitActivity.introTitle,
        welcomeTitleColor = primaryText,
        welcomeDescription = configuration.text.gaitActivity.introBody,
        welcomeDescriptionColor = primaryText,
        welcomeRemindButton = configuration.text.task.remindButton,
        welcomeRemindButtonColor = secondary,
        welcomeRemindButtonTextColor = primaryEnd,
        welcomeStartButton = configuration.text.task.startButton,
        welcomeStartButtonColor = primaryEnd,
        welcomeStartButtonTextColor = secondary,
        welcomeShadowColor = primaryText,
        welcomeRemindMeLater = reschedule.isEnabled(),
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
        endBackgroundColor = secondary,
        endTitle = null,
        endTitleColor = primaryText,
        endDescription = null,
        endDescriptionColor = primaryText,
        endButton = null,
        endButtonColor = primaryEnd,
        endButtonTextColor = secondary,
        endClose = false,
        endCheckMarkBackgroundColor = primaryEnd,
        endCheckMarkColor = secondary,
        moshi = moshi
    )
}