package org.fouryouandme.researchkit.recorder.sensor.motion

import com.squareup.moshi.Json

internal data class AccelerometerJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.Accelerometer
        ): AccelerometerJson =
            AccelerometerJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.x,
                data.y,
                data.z
            )

    }

}

internal data class LinearAccelerometerJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.LinearAccelerometer
        ): LinearAccelerometerJson =
            LinearAccelerometerJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.x,
                data.y,
                data.z
            )

    }

}

internal data class GravityJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.Gravity
        ): GravityJson =
            GravityJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.x,
                data.y,
                data.z
            )
    }

}

internal data class RotationVectorJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "reference_coordinate") val referenceCoordinate: String,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
    @Json(name = "w") val w: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.RotationVector
        ): RotationVectorJson =
            RotationVectorJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.referenceCoordinate,
                data.x,
                data.y,
                data.z,
                data.w
            )

    }

}

internal data class GameRotationVectorJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "reference_coordinate") val referenceCoordinate: String,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
    @Json(name = "w") val w: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.GameRotationVector
        ): GameRotationVectorJson =
            GameRotationVectorJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.referenceCoordinate,
                data.x,
                data.y,
                data.z,
                data.w
            )

    }

}

internal data class GeomagneticRotationVectorJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "reference_coordinate") val referenceCoordinate: String,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
    @Json(name = "w") val w: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.GeomagneticRotationVector
        ): GeomagneticRotationVectorJson =
            GeomagneticRotationVectorJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.referenceCoordinate,
                data.x,
                data.y,
                data.z,
                data.w
            )

    }

}

internal data class GyroscopeJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.Gyroscope
        ): GyroscopeJson =
            GyroscopeJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.x,
                data.y,
                data.z
            )

    }

}

internal data class UncalibratedJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x_uncalibrated") val xUncalibrated: Float,
    @Json(name = "y_uncalibrated") val yUncalibrated: Float,
    @Json(name = "z_Uncalibrated") val zUncalibrated: Float,
    @Json(name = "x_bias") val xBias: Float,
    @Json(name = "y_bias") val yBias: Float,
    @Json(name = "z_bias") val zBias: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.Uncalibrated
        ): UncalibratedJson =
            UncalibratedJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.xUncalibrated,
                data.yUncalibrated,
                data.zUncalibrated,
                data.xBias,
                data.yBias,
                data.zBias,
            )

    }

}

internal data class MagneticFieldJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "sensor_type") val sensorType: String,
    @Json(name = "sensor_accuracy") val sensorAccuracy: Int,
    @Json(name = "x") val x: Float,
    @Json(name = "y") val y: Float,
    @Json(name = "z") val z: Float,
) {

    companion object {

        fun from(
            data: DeviceMotionRecorderData.MagneticField
        ): MagneticFieldJson =
            MagneticFieldJson(
                data.timeStamp,
                data.sensorType,
                data.sensorAccuracy,
                data.x,
                data.y,
                data.z,
            )

    }

}
