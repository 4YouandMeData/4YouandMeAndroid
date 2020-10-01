package org.fouryouandme.researchkit.recorder.config

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.recorder.RecorderConfig
import org.fouryouandme.researchkit.recorder.sensor.accelerometer.AccelerometerRecorder
import org.fouryouandme.researchkit.step.Step
import java.io.File

/**
 * @property frequency
 * The frequency of accelerometer data collection in samples per second (Hz).
 */
class AccelerometerRecorderConfig(
    private val moshi: Moshi,
    private val frequency: Double
) : RecorderConfig(AccelerometerRecorder.ACCELEROMETER_IDENTIFIER) {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        AccelerometerRecorder(
            frequency,
            identifier,
            step,
            File(outputDirectory, AccelerometerRecorder.ACCELEROMETER_IDENTIFIER),
            moshi
        )

}