package org.fouryouandme.core.arch.deps.task

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.task.fitness.FitnessTask

suspend fun FYAMTaskConfiguration.buildFitness(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
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
        startBackgroundColor = secondary,
        startTitle = null,
        startTitleColor = primaryText,
        startDescription = null,
        startDescriptionColor = primaryText,
        startImage = imageConfiguration.heartBeat(),
        startButton = null,
        startButtonColor = primaryEnd,
        startButtonTextColor = secondary,
        introBackgroundColor = secondary,
        introTitle = null,
        introTitleColor = primaryText,
        introDescription = null,
        introDescriptionColor = primaryText,
        introImage = imageConfiguration.walkingMan(),
        introButton = null,
        introButtonColor = primaryEnd,
        introButtonTextColor = secondary,
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
        moshi
    )
}