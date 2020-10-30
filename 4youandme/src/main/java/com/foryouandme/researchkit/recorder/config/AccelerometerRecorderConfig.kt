package com.foryouandme.researchkit.recorder.config

import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.recorder.RecorderConfig
import com.foryouandme.researchkit.recorder.sensor.accelerometer.AccelerometerRecorder
import com.foryouandme.researchkit.step.Step
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