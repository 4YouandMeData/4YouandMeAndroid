package org.fouryouandme.researchkit.recorder.sensor.motion

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Build
import arrow.core.Tuple2
import arrow.core.toT
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.sensor.SensorRecorder
import org.fouryouandme.researchkit.step.Step
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.math.roundToInt

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
    private val moshi: Moshi,
) : SensorRecorder(
    "device_motion_thread",
    frequency,
    identifier,
    step,
    outputDirectory,
    30000
) {

    companion object {

        const val GRAVITY_SI_CONVERSION = SensorManager.GRAVITY_EARTH

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

    override suspend fun recordSensorEvent(sensorEvent: SensorEvent): String? {

        val sensorType = sensorEvent.sensor.type
        val sensorTypeKey = sensorTypeToDataType()[sensorType]

        val sensorData =
            sensorTypeKey?.let {

                when (sensorType) {
                    Sensor.TYPE_ACCELEROMETER ->
                        recordAccelerometerEvent(it, sensorEvent)
                    Sensor.TYPE_GRAVITY ->
                        recordGravityEvent(it, sensorEvent)
                        Sensor.TYPE_LINEAR_ACCELERATION ->
                            recordLinearAccelerometerEvent(it, sensorEvent)
                        Sensor.TYPE_GYROSCOPE ->
                            recordGyroscope(it, sensorEvent)
                        Sensor.TYPE_MAGNETIC_FIELD ->
                            recordMagneticField(it, sensorEvent)
                        Sensor.TYPE_GYROSCOPE_UNCALIBRATED,
                        Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED,
                        Sensor.TYPE_ACCELEROMETER_UNCALIBRATED ->
                            recordUncalibrated(it, sensorEvent)
                        Sensor.TYPE_GAME_ROTATION_VECTOR ->
                            recordGameRotationVector(it, sensorEvent)
                        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR ->
                            recordGeomagneticRotationVector(it, sensorEvent)
                        Sensor.TYPE_ROTATION_VECTOR ->
                            recordRotationVector(it, sensorEvent)
                        else -> {
                            Timber.e("Unable to record sensor type: $sensorType")
                            null
                        }
                    }

                }

            if (sensorData == null)
                Timber.e("Unable find type key for sensor type: $sensorType")

        return sensorData?.b

        }

    /**
     * @see [Sensor Types: Accelerometer]
     * (https://source.android.com/devices/sensors/sensor-types.accelerometer)
     */
    private suspend fun recordAccelerometerEvent(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.Accelerometer, String> =
        DeviceMotionRecorderData.Accelerometer(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            x = sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            y = sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            z = sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        ).tupleWithJson()

    /**
     * @see [Sensor Types: Accelerometer]
     * (https://source.android.com/devices/sensors/sensor-types.linear_acceleration)
     */
    private suspend fun recordLinearAccelerometerEvent(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.LinearAccelerometer, String> =
        // acceleration = gravity + linear-acceleration
        DeviceMotionRecorderData.LinearAccelerometer(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            x = sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            y = sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            z = sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        ).tupleWithJson()

    /**
     * Direction and magnitude of gravity.
     * @see [Sensor Types: Gravity ]
     * (https://source.android.com/devices/sensors/sensor-types.gravity)
     */
    private suspend fun recordGravityEvent(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.Gravity, String> =
        DeviceMotionRecorderData.Gravity(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            x = sensorEvent.values[0] / GRAVITY_SI_CONVERSION,
            y = sensorEvent.values[1] / GRAVITY_SI_CONVERSION,
            z = sensorEvent.values[2] / GRAVITY_SI_CONVERSION
        ).tupleWithJson()

    /**
     * Sensor.TYPE_ROTATION_VECTOR relative to East-North-Up coordinate frame.
     * @see [https://source.android.com/devices/sensors/sensor-types.rotation_vector]
     * (https://source.android.com/devices/sensors/sensor-types.attitude_composite_sensors)
     * */
    private suspend fun recordRotationVector(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.RotationVector, String> =

        DeviceMotionRecorderData.RotationVector(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.values[4].roundToInt(),
            referenceCoordinate = "zUp",
            // x = rot_axis.y * sin(theta/2)
            x = sensorEvent.values[0],
            // y = rot_axis.y * sin(theta/2)
            y = sensorEvent.values[1],
            // z = rot_axis.z * sin(theta/2)
            z = sensorEvent.values[2],
            // w = cos(theta/2)
            w = sensorEvent.values[3],
        ).tupleWithJson()

    /**
     * Sensor.TYPE_GAME_ROTATION_VECTOR  no magnetometer
     * @see [https://source.android.com/devices/sensors/sensor-types.game_rotation_vector]
     */
    private suspend fun recordGameRotationVector(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.GameRotationVector, String> =

        DeviceMotionRecorderData.GameRotationVector(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            referenceCoordinate = "zUp",
            // x = rot_axis.y * sin(theta/2)
            x = sensorEvent.values[0],
            // y = rot_axis.y * sin(theta/2)
            y = sensorEvent.values[1],
            // z = rot_axis.z * sin(theta/2)
            z = sensorEvent.values[2],
            // w = cos(theta/2)
            w = sensorEvent.values[3],
        ).tupleWithJson()

    /**
     * Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR similar to a rotation vector sensor but using a
     * magnetometer and no gyroscope
     * @see [https://source.android.com/devices/sensors/sensor-types.geomagnetic_rotation_vector]
     */
    private suspend fun recordGeomagneticRotationVector(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.GeomagneticRotationVector, String> =

        DeviceMotionRecorderData.GeomagneticRotationVector(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.values[4].roundToInt(),
            referenceCoordinate = "zUp",
            // x = rot_axis.y * sin(theta/2)
            x = sensorEvent.values[0],
            // y = rot_axis.y * sin(theta/2)
            y = sensorEvent.values[1],
            // z = rot_axis.z * sin(theta/2)
            z = sensorEvent.values[2],
            // w = cos(theta/2)
            w = sensorEvent.values[3],
        ).tupleWithJson()

    private suspend fun recordGyroscope(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.Gyroscope, String> =
        DeviceMotionRecorderData.Gyroscope(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            x = sensorEvent.values[0],
            y = sensorEvent.values[1],
            z = sensorEvent.values[2]
        ).tupleWithJson()

    // used for uncalibrated gyroscope, uncalibrated accelerometer, and uncalibrated magnetic field
    private suspend fun recordUncalibrated(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.Uncalibrated, String> =
        // conceptually: _uncalibrated  = _calibrated + _bias.
        DeviceMotionRecorderData.Uncalibrated(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            xUncalibrated = sensorEvent.values[0],
            yUncalibrated = sensorEvent.values[1],
            zUncalibrated = sensorEvent.values[2],
            xBias = sensorEvent.values[3],
            yBias = sensorEvent.values[4],
            zBias = sensorEvent.values[5]
        ).tupleWithJson()

    private suspend fun recordMagneticField(
        sensorType: String,
        sensorEvent: SensorEvent
    ): Tuple2<DeviceMotionRecorderData.MagneticField, String> =
        DeviceMotionRecorderData.MagneticField(
            getCurrentRecordingTime() ?: 0,
            sensorType,
            sensorEvent.accuracy,
            x = sensorEvent.values[0],
            y = sensorEvent.values[1],
            z = sensorEvent.values[2]
        ).tupleWithJson()

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        // no-op
    }

    private suspend inline fun <reified T : DeviceMotionRecorderData> T.tupleWithJson(
    ): Tuple2<T, String> =
        this toT when (this) {
            is DeviceMotionRecorderData.Accelerometer -> this.toJson(moshi)
            is DeviceMotionRecorderData.GameRotationVector -> this.toJson(moshi)
            is DeviceMotionRecorderData.GeomagneticRotationVector -> this.toJson(moshi)
            is DeviceMotionRecorderData.Gravity -> this.toJson(moshi)
            is DeviceMotionRecorderData.Gyroscope -> this.toJson(moshi)
            is DeviceMotionRecorderData.LinearAccelerometer -> this.toJson(moshi)
            is DeviceMotionRecorderData.MagneticField -> this.toJson(moshi)
            is DeviceMotionRecorderData.RotationVector -> this.toJson(moshi)
            is DeviceMotionRecorderData.Uncalibrated -> this.toJson(moshi)
            else -> ""
        }
}