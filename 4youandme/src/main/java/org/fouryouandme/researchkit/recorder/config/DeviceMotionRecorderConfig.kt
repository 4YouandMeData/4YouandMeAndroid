package org.fouryouandme.researchkit.recorder.config

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.recorder.RecorderConfig
import org.fouryouandme.researchkit.recorder.sensor.AccelerometerRecorder
import org.fouryouandme.researchkit.step.Step
import java.io.File

/**
 * @property frequency
 * The frequency of accelerometer data collection in samples per second (Hz).
 */
class DeviceMotionRecorderConfig(
    private val moshi: Moshi,
    private val frequency: Double
) : RecorderConfig("device_motion") {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        AccelerometerRecorder(frequency, identifier, step, outputDirectory, moshi)

}