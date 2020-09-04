package org.fouryouandme.researchkit.recorder.config

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.Recorder
import org.fouryouandme.researchkit.recorder.RecorderConfig
import org.fouryouandme.researchkit.recorder.sensor.LocationRecorder
import org.fouryouandme.researchkit.step.Step
import java.io.File

class LocationRecorderConfig(
    private val moshi: Moshi,
    private val minTime: Long = DEFAULT_MIN_TIME,
    private val minDistance: Float = DEFAULT_LOCATION_DISTANCE.toFloat(),
    private val usesRelativeCoordinates: Boolean = DEFAULT_USES_RELATIVE_COORDINATE,
) : RecorderConfig("location") {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        LocationRecorder(
            identifier,
            step,
            outputDirectory,
            moshi,
            minTime,
            minDistance,
            usesRelativeCoordinates
        )

    companion object {

        const val DEFAULT_MIN_TIME: Long = 100 // 100 milliseconds minimal time change
        const val DEFAULT_LOCATION_DISTANCE: Long = 0 // no min distance
        const val DEFAULT_USES_RELATIVE_COORDINATE = false // default to absolute coordinates

    }
}