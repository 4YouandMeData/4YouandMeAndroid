package org.fouryouandme.researchkit.recorder.sensor

import android.hardware.Sensor
import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.step.Step
import java.io.File

/**
 *
 * This class uses the JsonArrayDataRecorder class to save the Accelerometer sensor's data as
 * an array of accelerometer json objects with timestamp, ax, ay, and az
 */
class AccelerometerRecorder internal constructor(
    frequency: Double,
    identifier: String,
    step: Step,
    outputDirectory: File,
    moshi: Moshi
) : DeviceMotionRecorder(frequency, identifier, step, outputDirectory, moshi) {

    override fun getSensorTypeList(availableSensorList: List<Sensor>): List<Int> =
        if (hasAvailableType(availableSensorList, Sensor.TYPE_ACCELEROMETER))
            listOf(Sensor.TYPE_ACCELEROMETER)
        else
            emptyList()

}