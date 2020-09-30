package org.fouryouandme.core.arch.deps.task

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.TaskHandleResult
import org.fouryouandme.researchkit.task.gait.GaitTask

suspend fun FYAMTaskConfiguration.buildGait(
    id: String,
    configuration: Configuration,
    imageConfiguration: ImageConfiguration,
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
        startBackgroundColor = secondary,
        startTitle = null,
        startTitleColor = primaryText,
        startDescription = null,
        startDescriptionColor = primaryText,
        startButton = null,
        startButtonColor = primaryEnd,
        startButtonTextColor = secondary,
        introBackgroundColor = secondary,
        introTitle = null,
        introTitleColor = primaryText,
        introDescription = null,
        introDescriptionColor = primaryText,
        introImage = imageConfiguration.pocket(),
        introButton = null,
        introButtonColor = primaryEnd,
        introButtonTextColor = secondary,
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
        moshi
    )
}

suspend fun FYAMTaskConfiguration.sendGaitData(
    taskModule: TaskModule,
    taskId: String,
    result: TaskResult
): TaskHandleResult {


    return TaskHandleResult.Handled

}