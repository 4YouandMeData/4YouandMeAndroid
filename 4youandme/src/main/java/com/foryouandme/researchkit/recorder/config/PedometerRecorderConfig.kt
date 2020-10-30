package com.foryouandme.researchkit.recorder.config

import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.recorder.RecorderConfig
import com.foryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import com.foryouandme.researchkit.step.Step
import com.squareup.moshi.Moshi
import java.io.File

class PedometerRecorderConfig(
    val moshi: Moshi
) : RecorderConfig(PedometerRecorder.PEDOMETER_IDENTIFIER) {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        PedometerRecorder(
            identifier,
            step,
            File(outputDirectory, PedometerRecorder.PEDOMETER_IDENTIFIER),
            moshi
        )

}