package com.fouryouandme.researchkit.recorder.config

import com.fouryouandme.researchkit.recorder.Recorder
import com.fouryouandme.researchkit.recorder.RecorderConfig
import com.fouryouandme.researchkit.recorder.sensor.accelerometer.AccelerometerRecorder
import com.fouryouandme.researchkit.step.Step
import com.squareup.moshi.Moshi
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