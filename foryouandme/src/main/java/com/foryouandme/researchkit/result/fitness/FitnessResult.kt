package com.foryouandme.researchkit.result.fitness

import com.foryouandme.core.ext.readJson
import com.foryouandme.researchkit.result.FileResult
import com.foryouandme.researchkit.result.TaskResult
import com.foryouandme.researchkit.task.fitness.FitnessTask


data class FitnessResult(
    val fitnessWalkResult: FitnessWalkResult,
    val fitnessSitResult: FitnessSitResult,
)

data class FitnessWalkResult(
    val deviceMotion: String,
    val accelerometer: String,
    val pedometer: String,
)

data class FitnessSitResult(
    val deviceMotion: String,
    val accelerometer: String,
)

fun TaskResult.toFitnessResult(): FitnessResult? {

    // parse all file result content
    val walkPedometer =
        results[FitnessTask.FITNESS_WALK_PEDOMETER] as? FileResult
    val walkPedometerJson =
        walkPedometer?.file.readJson()

    val walkAccelerometer =
        results[FitnessTask.FITNESS_WALK_ACCELEROMETER] as? FileResult
    val walkAccelerometerJson =
        walkAccelerometer?.file.readJson()

    val walkDeviceMotion =
        results[FitnessTask.FITNESS_WALK_DEVICE_MOTION] as? FileResult
    val walkDeviceMotionJson =
        walkDeviceMotion?.file.readJson()


    val sitAccelerometer =
        results[FitnessTask.FITNESS_SIT_ACCELEROMETER] as? FileResult
    val sitAccelerometerJson =
        sitAccelerometer?.file.readJson()

    val sitDeviceMotion =
        results[FitnessTask.FITNESS_SIT_DEVICE_MOTION] as? FileResult
    val sitDeviceMotionJson =
        sitDeviceMotion?.file.readJson()

    // build the request object for the api
    return when (null) {
        walkDeviceMotionJson, walkAccelerometerJson, walkPedometerJson,
        sitDeviceMotionJson, sitAccelerometerJson -> null
        else ->
            FitnessResult(
                FitnessWalkResult(
                    deviceMotion = walkDeviceMotionJson,
                    accelerometer = walkAccelerometerJson,
                    pedometer = walkPedometerJson,
                ),
                FitnessSitResult(
                    deviceMotion = sitDeviceMotionJson,
                    accelerometer = sitAccelerometerJson,
                ),
            )
    }

}