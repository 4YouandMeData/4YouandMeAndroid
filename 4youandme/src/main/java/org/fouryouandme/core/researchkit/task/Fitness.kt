package org.fouryouandme.core.researchkit.task

import arrow.core.computations.either
import arrow.core.flatMap
import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.ErrorModule
import org.fouryouandme.core.arch.deps.modules.TaskModule
import org.fouryouandme.core.cases.task.TaskUseCase.updateFitnessTask
import org.fouryouandme.core.data.api.task.request.FitnessSitRequest
import org.fouryouandme.core.data.api.task.request.FitnessUpdateRequest
import org.fouryouandme.core.data.api.task.request.FitnessWalkRequest
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.ext.invokeAsFourYouAndMeError
import org.fouryouandme.core.ext.readJson
import org.fouryouandme.researchkit.result.FileResult
import org.fouryouandme.researchkit.result.TaskResult
import org.fouryouandme.researchkit.task.TaskHandleResult
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
        moshi
    )
}

suspend fun FYAMTaskConfiguration.sendFitnessData(
    taskModule: TaskModule,
    errorModule: ErrorModule,
    taskId: String,
    result: TaskResult
): TaskHandleResult {


    // parse all file result content

    val walkPedometer =
        result.results[FitnessTask.FITNESS_WALK_PEDOMETER] as? FileResult
    val walkPedometerJson =
        walkPedometer?.file.readJson(errorModule)

    val walkAccelerometer =
        result.results[FitnessTask.FITNESS_WALK_ACCELEROMETER] as? FileResult
    val walkAccelerometerJson =
        walkAccelerometer?.file.readJson(errorModule)

    val walkDeviceMotion =
        result.results[FitnessTask.FITNESS_WALK_DEVICE_MOTION] as? FileResult
    val walkDeviceMotionJson =
        walkDeviceMotion?.file.readJson(errorModule)


    val sitAccelerometer =
        result.results[FitnessTask.FITNESS_SIT_ACCELEROMETER] as? FileResult
    val sitAccelerometerJson =
        sitAccelerometer?.file.readJson(errorModule)

    val sitDeviceMotion =
        result.results[FitnessTask.FITNESS_SIT_DEVICE_MOTION] as? FileResult
    val sitDeviceMotionJson =
        sitDeviceMotion?.file.readJson(errorModule)

    // build the request object for the api

    val request =
        either.invokeAsFourYouAndMeError {

            FitnessUpdateRequest(
                FitnessWalkRequest(
                    deviceMotion = !walkDeviceMotionJson,
                    accelerometer = !walkAccelerometerJson,
                    pedometer = !walkPedometerJson,
                ),
                FitnessSitRequest(
                    deviceMotion = !sitDeviceMotionJson,
                    accelerometer = !sitAccelerometerJson,
                ),
            )
        }

    // upload data to task api

    val response =
        request.flatMap { taskModule.updateFitnessTask(taskId, it) }

    // convert the result to TaskHandleResult

    return response.fold({ TaskHandleResult.Error(it.message) }, { TaskHandleResult.Handled })

}