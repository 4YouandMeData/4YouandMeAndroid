package com.foryouandme.core.researchkit.task

import arrow.core.computations.either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.ErrorModule
import com.foryouandme.core.arch.deps.modules.TaskModule
import com.foryouandme.core.cases.task.TaskUseCase.updateGaitTask
import com.foryouandme.core.data.api.task.request.GaitOutboundRequest
import com.foryouandme.core.data.api.task.request.GaitRestRequest
import com.foryouandme.core.data.api.task.request.GaitReturnRequest
import com.foryouandme.core.data.api.task.request.GaitUpdateRequest
import com.foryouandme.core.entity.activity.Reschedule
import com.foryouandme.core.entity.activity.Reschedule.Companion.isEnabled
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.ext.invokeAsForYouAndMeError
import com.foryouandme.core.ext.readJson
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.TaskResponse
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

suspend fun FYAMTaskConfiguration.sendGaitData(
    taskModule: TaskModule,
    errorModule: ErrorModule,
    taskId: String,
    result: TaskResult
): TaskResponse {


    // parse all file result content

    val outBoundPedometer =
        result.results[GaitTask.GAIT_OUTBOUND_PEDOMETER] as? FileResult
    val outBoundPedometerJson =
        outBoundPedometer?.file.readJson(errorModule)

    val outBoundAccelerometer =
        result.results[GaitTask.GAIT_OUTBOUND_ACCELEROMETER] as? FileResult
    val outBoundAccelerometerJson =
        outBoundAccelerometer?.file.readJson(errorModule)

    val outBoundDeviceMotion =
        result.results[GaitTask.GAIT_OUTBOUND_DEVICE_MOTION] as? FileResult
    val outBoundDeviceMotionJson =
        outBoundDeviceMotion?.file.readJson(errorModule)


    val returnPedometer =
        result.results[GaitTask.GAIT_RETURN_PEDOMETER] as? FileResult
    val returnPedometerJson =
        returnPedometer?.file.readJson(errorModule)

    val returnAccelerometer =
        result.results[GaitTask.GAIT_RETURN_ACCELEROMETER] as? FileResult
    val returnAccelerometerJson =
        returnAccelerometer?.file.readJson(errorModule)

    val returnDeviceMotion =
        result.results[GaitTask.GAIT_RETURN_DEVICE_MOTION] as? FileResult
    val returnDeviceMotionJson =
        returnDeviceMotion?.file.readJson(errorModule)


    val restAccelerometer =
        result.results[GaitTask.GAIT_REST_ACCELEROMETER] as? FileResult
    val restAccelerometerJson =
        restAccelerometer?.file.readJson(errorModule)

    val restDeviceMotion =
        result.results[GaitTask.GAIT_REST_DEVICE_MOTION] as? FileResult
    val restDeviceMotionJson =
        restDeviceMotion?.file.readJson(errorModule)

    // build the request object for the api

    val request =
        either.invokeAsForYouAndMeError {

            GaitUpdateRequest(
                GaitOutboundRequest(
                    deviceMotion = !outBoundDeviceMotionJson,
                    accelerometer = !outBoundAccelerometerJson,
                    pedometer = !outBoundPedometerJson
                ),
                GaitReturnRequest(
                    deviceMotion = !returnDeviceMotionJson,
                    accelerometer = !returnAccelerometerJson,
                    pedometer = !returnPedometerJson
                ),
                GaitRestRequest(
                    deviceMotion = !restDeviceMotionJson,
                    accelerometer = !restAccelerometerJson,
                ),

                )
        }

    // upload data to task api

    val response =
        request.flatMap { taskModule.updateGaitTask(taskId, it) }

    // convert the result to TaskHandleResult

    return response.fold({ TaskResponse.Error(it.message) }, { TaskResponse.Success })

}