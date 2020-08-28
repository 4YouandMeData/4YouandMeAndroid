package org.fouryouandme.researchkit.recorder.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.detector.AccelerometerStepDetector
import org.fouryouandme.researchkit.step.Step
import java.io.File

open class PedometerRecorder internal constructor(
    identifier: String,
    step: Step,
    outputDirectory: File,
    moshi: Moshi
) : SensorRecorder(
    MANUAL_JSON_FREQUENCY.toDouble(),
    identifier,
    step,
    outputDirectory,
    moshi
) {

    private var useAccelerometerDetector = false
    private val accelerometerStepDetector: AccelerometerStepDetector by lazy {
        AccelerometerStepDetector()
    }
    private var strideLength = 0f // in meters
    private var stepCounter = 0

    override fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int> {
        // Not all devices even have the pedometer sensor
        // I haven't found a full list yet but here are some that have it
        // HTC One M8, Nexus 5x, Nexus 6p, Samsung S6 and S7
        if (hasAvailableType(availableSensorList, Sensor.TYPE_STEP_DETECTOR)) {
            useAccelerometerDetector = false
            return listOf(Sensor.TYPE_STEP_DETECTOR)
        }
        // do a custom pedometer algorithm
        useAccelerometerDetector = true
        return listOf(Sensor.TYPE_ACCELEROMETER)
    }

    override fun start(context: Context) {
        super.start(context)

        stepCounter = 0
        if (useAccelerometerDetector) {
            accelerometerStepDetector.setOnStepTakenListener { onAccelerometerStepTaken() }
        }
        strideLength = computeStrideLength()
    }

    /**
     * TODO: use the height and the gender of the user to compute this value
     * height * HEIGHT_FACTOR_FOR_STRIDE_LENGTH_MALE
     * @return stride length
     */
    private fun computeStrideLength(): Float = DEFAULT_METERS_PER_STRIDE

    private fun onStepTaken(): MutableMap<String, Any> {

        val json = mutableMapOf<String, Any>()

        stepCounter++
        json[TIMESTAMP_KEY] = System.currentTimeMillis()
        json[END_DATE] = System.currentTimeMillis()
        json[NUMBER_OF_STEPS] = stepCounter
        val distance = strideLength * stepCounter
        json[DISTANCE] = distance

        return json
    }

    private fun onAccelerometerStepTaken(): Unit {

        val json = mutableMapOf<String, Any>()

        stepCounter++
        json[TIMESTAMP_KEY] = System.currentTimeMillis()
        json[END_DATE] = System.currentTimeMillis()
        json[NUMBER_OF_STEPS] = stepCounter
        val distance = strideLength * stepCounter
        json[DISTANCE] = distance

        super.writeJsonObjectToFile(json)

    }

    override fun recordSensorEvent(sensorEvent: SensorEvent): Map<String, Any> =

        when (sensorEvent.sensor.type) {
            Sensor.TYPE_STEP_DETECTOR -> onStepTaken()
            Sensor.TYPE_ACCELEROMETER -> {
                accelerometerStepDetector.processAccelerometerData(sensorEvent)
                emptyMap()
            }
            else -> emptyMap()
        }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        // NO-OP
    }

    companion object {

        const val TIMESTAMP_KEY = "timestamp"
        const val END_DATE = "endDate"
        const val NUMBER_OF_STEPS = "numberOfSteps"
        const val DISTANCE = "totalDistance"

        /**
         * This used to compute the totalDistance the user has traveled while recording the pedometer
         * The default value is about half a meter, it will only be used when a user's height is unavailable
         */
        const val DEFAULT_METERS_PER_STRIDE = 0.6f // in meters

        /**
         * This factor, when multiplied by a user's height, will determine an average stride length
         */
        const val HEIGHT_FACTOR_FOR_STRIDE_LENGTH_MALE = 0.413f
        const val HEIGHT_FACTOR_FOR_STRIDE_LENGTH_FEMALE = 0.415f
    }
}