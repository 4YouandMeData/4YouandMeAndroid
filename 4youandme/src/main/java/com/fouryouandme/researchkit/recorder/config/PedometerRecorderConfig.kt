package com.fouryouandme.researchkit.recorder.config

import com.fouryouandme.researchkit.recorder.Recorder
import com.fouryouandme.researchkit.recorder.RecorderConfig
import com.fouryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import com.fouryouandme.researchkit.step.Step
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