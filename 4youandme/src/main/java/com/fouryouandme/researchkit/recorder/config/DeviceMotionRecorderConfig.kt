package com.fouryouandme.researchkit.recorder.config

import com.fouryouandme.researchkit.recorder.Recorder
import com.fouryouandme.researchkit.recorder.RecorderConfig
import com.fouryouandme.researchkit.recorder.sensor.motion.DeviceMotionRecorder
import com.fouryouandme.researchkit.step.Step
import com.squareup.moshi.Moshi
import java.io.File

/**
 * @property frequency
 * The frequency of accelerometer data collection in samples per second (Hz).
 */
class DeviceMotionRecorderConfig(
    private val moshi: Moshi,
    private val frequency: Double
) : RecorderConfig(DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER) {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        DeviceMotionRecorder(
            frequency,
            identifier,
            step,
            File(outputDirectory, DeviceMotionRecorder.DEVICE_MOTION_IDENTIFIER),
            moshi
        )

}