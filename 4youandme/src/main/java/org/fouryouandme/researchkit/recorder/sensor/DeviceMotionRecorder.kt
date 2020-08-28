package org.fouryouandme.researchkit.recorder.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import arrow.core.getOrElse
import arrow.core.toOption
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.step.Step
import timber.log.Timber
import java.io.File
import java.util.*

/**
 *
 * The DeviceMotionRecorder incorporates a bunch of sensor fusion sensor readings
 * together to paint a broad picture of the device's orientation and movement over time.
 *
 * This class is an attempt at recording data in a similar way as iOS' device motion recorder.
 *
 * @see [Sensor values]
 * (https://developer.android.com/reference/android/hardware/SensorEvent.html.values)
 *
 * @see [Sensor Types]
 * (https://source.android.com/devices/sensors/sensor-type)
 *
 * @see [Position Sensors]
 * (https://developer.android.com/guide/topics/sensors/sensors_position.html)
 *
 * @see [Motion Sensors]
 * (https://developer.android.com/guide/topics/sensors/sensors_motion.html)
 *
 */
open class DeviceMotionRecorder internal constructor(
    frequency: Double,
    identifier: String,
    step: Step,
    outputDirectory: File,
    moshi: Moshi
) : SensorRecorder(frequency, identifier, step, outputDirectory, moshi) {

    companion object {
        const val GRAVITY_SI_CONVERSION = SensorManager.GRAVITY_EARTH
        const val SENSOR_DATA_TYPE_KEY = "sensorType"
        const val SENSOR_DATA_SUBTYPE_KEY = "sensorAndroidType"
        const val SENSOR_EVENT_ACCURACY_KEY = "eventAccuracy"
        const val ROTATION_REFERENCE_COORDINATE_KEY = "referenceCoordinate"
        const val X_KEY = "x"
        const val Y_KEY = "y"
        const val Z_KEY = "z"
        const val W_KEY = "w"
        const val ACCURACY_KEY = "estimatedAccuracy"
        const val X_UNCALIBRATED_KEY = "xUncalibrated"
        const val Y_UNCALIBRATED_KEY = "yUncalibrated"
        const val Z_UNCALIBRATED_KEY = "zUncalibrated"
        const val X_BIAS_KEY = "xBias"
        const val Y_BIAS_KEY = "yBias"
        const val Z_BIAS_KEY = "zBias"

        val rotationVectorType: List<Int> =
            listOf(
                Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_GAME_ROTATION_VECTOR,
                Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR
            )

        fun sensorTypeToDataType(): Map<Int, String> {

            val map = mutableMapOf<Int, String>()

            map[Sensor.TYPE_GYROSCOPE] = "rotationRate"
            map[Sensor.TYPE_GYROSCOPE_UNCALIBRATED] = "rotationRateUncalibrated"
            map[Sensor.TYPE_ACCELEROMETER] = "acceleration"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                map[Sensor.TYPE_ACCELEROMETER_UNCALIBRATED] = "accelerationUncalibrated"
            map[Sensor.TYPE_GRAVITY] = "gravity"
            map[Sensor.TYPE_LINEAR_ACCELERATION] = "userAcceleration"
            map[Sensor.TYPE_MAGNETIC_FIELD] = "magneticField"
            map[Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED] = "magneticFieldUncalibrated"
            map[Sensor.TYPE_ROTATION_VECTOR] = "attitude"
            map[Sensor.TYPE_GAME_ROTATION_VECTOR] = "attitude"
            map[Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR] = "attitude"

            return map

        }

    }

    override fun start(context: Context) {
        super.start(context)
    }

    override fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int> {

        val sensorTypeList: MutableList<Int> = ArrayList()

        // Only add these sensors if the device has them
        if (hasAvailableType(availableSensorList, Sensor.TYPE_ACCELEROMETER))
            sensorTypeList.add(Sensor.TYPE_ACCELEROMETER)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            && hasAvailableType(availableSensorList, Sensor.TYPE_ACCELEROMETER_UNCALIBRATED)
        ) sensorTypeList.add(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_GRAVITY))
            sensorTypeList.add(Sensor.TYPE_GRAVITY)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_LINEAR_ACCELERATION))
            sensorTypeList.add(Sensor.TYPE_LINEAR_ACCELERATION)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_GYROSCOPE))
            sensorTypeList.add(Sensor.TYPE_GYROSCOPE)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_GYROSCOPE_UNCALIBRATED))
            sensorTypeList.add(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_MAGNETIC_FIELD))
            sensorTypeList.add(Sensor.TYPE_MAGNETIC_FIELD)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED))
            sensorTypeList.add(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_ROTATION_VECTOR))
            sensorTypeList.add(Sensor.TYPE_ROTATION_VECTOR)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_GAME_ROTATION_VECTOR))
            sensorTypeList.add(Sensor.TYPE_GAME_ROTATION_VECTOR)

        if (hasAvailableType(availableSensorList, Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR))
            sensorTypeList.add(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

        return sensorTypeList
    }

    override fun recordSensorEvent(sensorEvent: SensorEvent): Map<String, Any> {

        val sensorType = sensorEvent.sensor.type
        val sensorTypeKey = sensorTypeToDataType()[sensorType].toOption()

        return sensorTypeKey.map {

            val json = mutableMapOf<String, Any>()

            json[SENSOR_DATA_TYPE_KEY] = sensorTypeKey
            json[SENSOR_EVENT_ACCURACY_KEY] = sensorEvent.accuracy

            val sensorJson =
                when (sensorType) {
                    Sensor.TYPE_ACCELEROMETER -> recordAccelerometerEvent(sensorEvent)
                    Sensor.TYPE_GRAVITY -> recordGravityEvent(sensorEvent)
                    Sensor.TYPE_LINEAR_ACCELERATION -> recordLinearAccelerometerEvent(sensorEvent)
                    Sensor.TYPE_GYROSCOPE -> recordGyroscope(sensorEvent)
                    Sensor.TYPE_MAGNETIC_FIELD -> recordMagneticField(sensorEvent)
                    Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
                    Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
                    Sensor.TYPE_ACCELEROMETER_UNCALIBRATED -> recordUncalibrated(sensorEvent)
                    Sensor.TYPE_GAME_ROTATION_VECTOR,
                    Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR,
                    Sensor.TYPE_ROTATION_VECTOR -> recordRotationVector(sensorEvent)
                    else -> {
                        Timber.e("Unable to record sensor type: $sensorType")
                        emptyMap()
                    }
                }

            json.plus(sensorJson)

        }.getOrElse {

            Timber.e("Unable find type key for sensor type: $sensorType")
            emptyMap()
        }
    }

    /**
     * @see [Sensor Types: Accelerometer]
     * (https://source.android.com/devices/sensors/sensor-types.accelerometer)
     */
    private fun recordAccelerometerEvent(sensorEvent: SensorEvent): Map<String, Any> =
        mapOf(
            X_KEY to sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            Y_KEY to sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            Z_KEY to sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        )

    /**
     * @see [Sensor Types: Accelerometer]
     * (https://source.android.com/devices/sensors/sensor-types.linear_acceleration)
     */
    private fun recordLinearAccelerometerEvent(sensorEvent: SensorEvent): Map<String, Any> =
        // acceleration = gravity + linear-acceleration
        mapOf(
            X_KEY to sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            Y_KEY to sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            Z_KEY to sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        )

    /**
     * Direction and magnitude of gravity.
     * @see [Sensor Types: Gravity ]
     * (https://source.android.com/devices/sensors/sensor-types.gravity)
     */
    private fun recordGravityEvent(sensorEvent: SensorEvent): Map<String, Any> =
        mapOf(
            X_KEY to sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            Y_KEY to sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            Z_KEY to sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        )

    /**
     * Sensor.TYPE_ROTATION_VECTOR relative to East-North-Up coordinate frame.
     * Sensor.TYPE_GAME_ROTATION_VECTOR  no magnetometer
     * Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR similar to a rotation vector sensor but using a
     * magnetometer and no gyroscope
     *
     * @see [
     * https://source.android.com/devices/sensors/sensor-types.rotation_vector
     * https://source.android.com/devices/sensors/sensor-types.game_rotation_vector
     * https://source.android.com/devices/sensors/sensor-types.geomagnetic_rotation_vector
    ](https://source.android.com/devices/sensors/sensor-types.attitude_composite_sensors) */
    private fun recordRotationVector(sensorEvent: SensorEvent): Map<String, Any> {

        val json = mutableMapOf<String, Any>()

        // indicate android sensor subtype
        val sensorType = sensorEvent.sensor.type

        when {
            Sensor.TYPE_ROTATION_VECTOR == sensorType -> {
                json[SENSOR_DATA_SUBTYPE_KEY] = "rotationVector"
                json[ROTATION_REFERENCE_COORDINATE_KEY] = "East-Up-North"
            }
            Sensor.TYPE_GAME_ROTATION_VECTOR == sensorType -> {
                json[SENSOR_DATA_SUBTYPE_KEY] = "gameRotationVector"
                json[ROTATION_REFERENCE_COORDINATE_KEY] = "zUp"
            }
            Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR == sensorType -> {
                json[SENSOR_DATA_SUBTYPE_KEY] = "geomagneticRotationVector"
                json[ROTATION_REFERENCE_COORDINATE_KEY] = "East-Up-North"
            }
        }

        // x = rot_axis.y * sin(theta/2)
        json[X_KEY] = sensorEvent.values[0]
        // y = rot_axis.y * sin(theta/2)
        json[Y_KEY] = sensorEvent.values[1]
        // z = rot_axis.z * sin(theta/2)
        json[Z_KEY] = sensorEvent.values[2]


        // w = cos(theta/2)
        json[W_KEY] = sensorEvent.values[3]

        // game rotation vector never provides accuracy always returns zero
        if (Sensor.TYPE_GAME_ROTATION_VECTOR != sensorType)
        // estimated accuracy in radians or -1 if unavailable
            json[ACCURACY_KEY] = sensorEvent.values[4]

        return json
    }

    private fun recordGyroscope(sensorEvent: SensorEvent): Map<String, Any> =
        mapOf(
            X_KEY to sensorEvent.values[0],
            Y_KEY to sensorEvent.values[1],
            Z_KEY to sensorEvent.values[2]
        )

    // used for uncalibrated gyroscope, uncalibrated accelerometer, and uncalibrated magnetic field
    private fun recordUncalibrated(sensorEvent: SensorEvent): Map<String, Any> =
        // conceptually: _uncalibrated  = _calibrated + _bias.
        mapOf(
            X_UNCALIBRATED_KEY to sensorEvent.values[0],
            Y_UNCALIBRATED_KEY to sensorEvent.values[1],
            Z_UNCALIBRATED_KEY to sensorEvent.values[2],
            X_BIAS_KEY to sensorEvent.values[3],
            Y_BIAS_KEY to sensorEvent.values[4],
            Z_BIAS_KEY to sensorEvent.values[5]
        )

    private fun recordMagneticField(sensorEvent: SensorEvent): Map<String, Any> =
        mapOf(
            X_KEY to sensorEvent.values[0],
            Y_KEY to sensorEvent.values[1],
            Z_KEY to sensorEvent.values[2]
        )

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        // no-op
    }
}