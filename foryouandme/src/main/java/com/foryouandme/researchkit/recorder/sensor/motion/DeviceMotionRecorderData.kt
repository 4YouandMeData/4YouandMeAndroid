package com.foryouandme.researchkit.recorder.sensor.motion

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.foryouandme.researchkit.recorder.sensor.RecorderData
import com.squareup.moshi.Moshi

sealed class DeviceMotionRecorderData(
    timeStamp: Long,
    val sensorType: String,
    val sensorAccuracy: Int,
) : RecorderData(timeStamp) {


    class Accelerometer(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val x: Float,
        val y: Float,
        val z: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(AccelerometerJson::class.java)
                    .toJson(AccelerometerJson.from(this@Accelerometer))
            }
    }

    class LinearAccelerometer(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val x: Float,
        val y: Float,
        val z: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(LinearAccelerometerJson::class.java)
                    .toJson(LinearAccelerometerJson.from(this@LinearAccelerometer))
            }
    }

    class Gravity(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val x: Float,
        val y: Float,
        val z: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(GravityJson::class.java)
                    .toJson(GravityJson.from(this@Gravity))
            }
    }

    class RotationVector(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val referenceCoordinate: String,
        val x: Float,
        val y: Float,
        val z: Float,
        val w: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(RotationVectorJson::class.java)
                    .toJson(RotationVectorJson.from(this@RotationVector))
            }
    }

    class GameRotationVector(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val referenceCoordinate: String,
        val x: Float,
        val y: Float,
        val z: Float,
        val w: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(GameRotationVectorJson::class.java)
                    .toJson(GameRotationVectorJson.from(this@GameRotationVector))
            }
    }

    class GeomagneticRotationVector(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val referenceCoordinate: String,
        val x: Float,
        val y: Float,
        val z: Float,
        val w: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(GeomagneticRotationVectorJson::class.java)
                    .toJson(GeomagneticRotationVectorJson.from(this@GeomagneticRotationVector))
            }
    }

    class Gyroscope(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val x: Float,
        val y: Float,
        val z: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(GyroscopeJson::class.java)
                    .toJson(GyroscopeJson.from(this@Gyroscope))
            }
    }

    class Uncalibrated(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val xUncalibrated: Float,
        val yUncalibrated: Float,
        val zUncalibrated: Float,
        val xBias: Float,
        val yBias: Float,
        val zBias: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(UncalibratedJson::class.java)
                    .toJson(UncalibratedJson.from(this@Uncalibrated))
            }
    }

    class MagneticField(
        timeStamp: Long,
        sensorType: String,
        sensorAccuracy: Int,
        val x: Float,
        val y: Float,
        val z: Float,
    ) : DeviceMotionRecorderData(timeStamp, sensorType, sensorAccuracy) {

        suspend fun toJson(moshi: Moshi): String =
            withContext(Dispatchers.IO) {
                moshi.adapter(MagneticFieldJson::class.java)
                    .toJson(MagneticFieldJson.from(this@MagneticField))
            }
    }

}