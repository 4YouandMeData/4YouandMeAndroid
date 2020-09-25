package org.fouryouandme.researchkit.recorder.sensor.motion

import com.squareup.moshi.Moshi
import org.fouryouandme.core.ext.evalOnIO
import org.fouryouandme.researchkit.recorder.sensor.RecorderData

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
            evalOnIO {
                moshi.adapter(AccelerometerJson::class.java)
                    .toJson(AccelerometerJson.from(this))
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
            evalOnIO {
                moshi.adapter(LinearAccelerometerJson::class.java)
                    .toJson(LinearAccelerometerJson.from(this))
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
            evalOnIO {
                moshi.adapter(GravityJson::class.java)
                    .toJson(GravityJson.from(this))
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
            evalOnIO {
                moshi.adapter(RotationVectorJson::class.java)
                    .toJson(RotationVectorJson.from(this))
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
            evalOnIO {
                moshi.adapter(GameRotationVectorJson::class.java)
                    .toJson(GameRotationVectorJson.from(this))
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
            evalOnIO {
                moshi.adapter(GeomagneticRotationVectorJson::class.java)
                    .toJson(GeomagneticRotationVectorJson.from(this))
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
            evalOnIO {
                moshi.adapter(GyroscopeJson::class.java)
                    .toJson(GyroscopeJson.from(this))
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
            evalOnIO {
                moshi.adapter(UncalibratedJson::class.java)
                    .toJson(UncalibratedJson.from(this))
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
            evalOnIO {
                moshi.adapter(MagneticFieldJson::class.java)
                    .toJson(MagneticFieldJson.from(this))
            }
    }

}