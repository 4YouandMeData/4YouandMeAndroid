package org.fouryouandme.researchkit.recorder.config

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.recorder.RecorderConfig
import org.fouryouandme.researchkit.recorder.sensor.pedometer.PedometerRecorder
import org.fouryouandme.researchkit.step.Step
import java.io.File

class PedometerRecorderConfig(val moshi: Moshi) : RecorderConfig("pedometer") {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        PedometerRecorder(identifier, step, outputDirectory, moshi)

}