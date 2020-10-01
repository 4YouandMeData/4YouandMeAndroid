package org.fouryouandme.researchkit.recorder.sensor.pedometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.sensor.SensorRecorder
import org.fouryouandme.researchkit.step.Step
import java.io.File

open class PedometerRecorder internal constructor(
    identifier: String,
    step: Step,
    outputDirectory: File,
    private val moshi: Moshi
) : SensorRecorder(
    "pedometer_thread",
    MANUAL_JSON_FREQUENCY.toDouble(),
    identifier,
    step,
    outputDirectory
) {

    private var strideLength = 0f // in meters
    private var stepCounter = 0

    override fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int> =
    // Not all devices even have the pedometer sensor
    // I haven't found a full list yet but here are some that have it
        // HTC One M8, Nexus 5x, Nexus 6p, Samsung S6 and S7
        if (hasAvailableType(availableSensorList, Sensor.TYPE_STEP_DETECTOR))
            listOf(Sensor.TYPE_STEP_DETECTOR)
        else listOf()

    override suspend fun start(context: Context) {
        super.start(context)

        stepCounter = 0
        strideLength = computeStrideLength()

    }

    /**
     * TODO: use the height and the gender of the user to compute this value
     * height * HEIGHT_FACTOR_FOR_STRIDE_LENGTH_MALE
     * @return stride length
     */
    private fun computeStrideLength(): Float = DEFAULT_METERS_PER_STRIDE

    private suspend fun onStepTaken(): PedometerRecorderData {

        stepCounter++
        val distance = strideLength * stepCounter

        return PedometerRecorderData(
            getCurrentRecordingTime() ?: 0,
            stepCounter,
            distance
        )
    }

    override suspend fun recordSensorEvent(sensorEvent: SensorEvent): String? {

        val data =
            when (sensorEvent.sensor.type) {
                Sensor.TYPE_STEP_DETECTOR -> onStepTaken()
                else -> null
            }

        data?.let { onRecordDataCollected(it) }

        return data?.toJson(moshi)

        }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {
        // NO-OP
    }

    companion object {

        /**
         * This used to compute the totalDistance the user has traveled while recording
         * the pedometer.
         * The default value is about half a meter, it will only be used when a user's height
         * is unavailable
         */
        const val DEFAULT_METERS_PER_STRIDE = 0.6f // in meters

        /**
         * This factor, when multiplied by a user's height, will determine an average stride length
         */
        const val HEIGHT_FACTOR_FOR_STRIDE_LENGTH_MALE = 0.413f
        const val HEIGHT_FACTOR_FOR_STRIDE_LENGTH_FEMALE = 0.415f

        const val PEDOMETER_IDENTIFIER: String = "pedometer"

    }
}