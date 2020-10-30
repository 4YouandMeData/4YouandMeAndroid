package com.foryouandme.researchkit.recorder.config

import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.recorder.RecorderConfig
import com.foryouandme.researchkit.recorder.sensor.motion.DeviceMotionRecorder
import com.foryouandme.researchkit.step.Step
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