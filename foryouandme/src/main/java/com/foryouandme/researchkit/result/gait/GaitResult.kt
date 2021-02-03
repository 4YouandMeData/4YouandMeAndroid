package com.foryouandme.researchkit.result.gait

import com.foryouandme.core.ext.readJson
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.gait.GaitTask

data class GaitResult(
    val gaitOutbound: GaitOutboundResult,
    val gaitReturn: GaitReturnResult,
    val gaitRest: GaitRestResult,
)

data class GaitOutboundResult(
    val deviceMotion: String,
    val accelerometer: String,
    val pedometer: String,
)

data class GaitReturnResult(
    val deviceMotion: String,
    val accelerometer: String,
    val pedometer: String,
)

data class GaitRestResult(
    val deviceMotion: String,
    val accelerometer: String,
)

fun TaskResult.toGaitResult(): GaitResult? {

    // parse all file result content

    val outBoundPedometer =
        results[GaitTask.GAIT_OUTBOUND_PEDOMETER] as? FileResult
    val outBoundPedometerJson =
        outBoundPedometer?.file.readJson()

    val outBoundAccelerometer =
        results[GaitTask.GAIT_OUTBOUND_ACCELEROMETER] as? FileResult
    val outBoundAccelerometerJson =
        outBoundAccelerometer?.file.readJson()

    val outBoundDeviceMotion =
        results[GaitTask.GAIT_OUTBOUND_DEVICE_MOTION] as? FileResult
    val outBoundDeviceMotionJson =
        outBoundDeviceMotion?.file.readJson()


    val returnPedometer =
        results[GaitTask.GAIT_RETURN_PEDOMETER] as? FileResult
    val returnPedometerJson =
        returnPedometer?.file.readJson()

    val returnAccelerometer =
        results[GaitTask.GAIT_RETURN_ACCELEROMETER] as? FileResult
    val returnAccelerometerJson =
        returnAccelerometer?.file.readJson()

    val returnDeviceMotion =
        results[GaitTask.GAIT_RETURN_DEVICE_MOTION] as? FileResult
    val returnDeviceMotionJson =
        returnDeviceMotion?.file.readJson()


    val restAccelerometer =
        results[GaitTask.GAIT_REST_ACCELEROMETER] as? FileResult
    val restAccelerometerJson =
        restAccelerometer?.file.readJson()

    val restDeviceMotion =
        results[GaitTask.GAIT_REST_DEVICE_MOTION] as? FileResult
    val restDeviceMotionJson =
        restDeviceMotion?.file.readJson()

    // build the request object for the api

    return when (null) {
        outBoundDeviceMotionJson, outBoundAccelerometerJson, outBoundPedometerJson,
        restAccelerometerJson, restDeviceMotionJson,
        returnDeviceMotionJson, returnPedometerJson, returnAccelerometerJson -> null
        else ->
            GaitResult(
                GaitOutboundResult(
                    deviceMotion = outBoundDeviceMotionJson,
                    accelerometer = outBoundAccelerometerJson,
                    pedometer = outBoundPedometerJson
                ),
                GaitReturnResult(
                    deviceMotion = returnDeviceMotionJson,
                    accelerometer = returnAccelerometerJson,
                    pedometer = returnPedometerJson
                ),
                GaitRestResult(
                    deviceMotion = restDeviceMotionJson,
                    accelerometer = restAccelerometerJson,
                ),
            )
    }

}