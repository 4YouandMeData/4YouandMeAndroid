package com.foryouandme.core.researchkit.task

import arrow.core.computations.either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.ErrorModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.cases.task.TaskUseCase.updateFitnessTask
import com.foryouandme.data.repository.task.network.request.FitnessSitRequest
import com.foryouandme.data.repository.task.network.request.FitnessUpdateRequest
import com.foryouandme.data.repository.task.network.request.FitnessWalkRequest
import com.foryouandme.entity.activity.Reschedule
import com.foryouandme.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.core.ext.invokeAsForYouAndMeError
import com.foryouandme.core.ext.readJson
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.fitness.FitnessTask
import com.squareup.moshi.Moshi

suspend fun FYAMTaskConfiguration.buildFitness(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
    reschedule: Reschedule?,
    moshi: Moshi
): FitnessTask {

    val secondary =
        configuration.theme.secondaryColor.color()

    val primaryText =
        configuration.theme.primaryTextColor.color()

    val primaryEnd =
        configuration.theme.primaryColorEnd.color()

    return FitnessTask(
        id = id,
        welcomeBackImage = imageConfiguration.backSecondary(),
        welcomeBackgroundColor = secondary,
        welcomeImage = imageConfiguration.videoDiaryIntro(),
        welcomeTitle = configuration.text.fitnessActivity.introTitle,
        welcomeTitleColor = primaryText,
        welcomeDescription = configuration.text.fitnessActivity.introBody,
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