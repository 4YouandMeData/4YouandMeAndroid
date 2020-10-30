package com.foryouandme.researchkit.recorder.config

import com.foryouandme.researchkit.recorder.Recorder
import com.foryouandme.researchkit.recorder.RecorderConfig
import com.foryouandme.researchkit.recorder.sensor.location.LocationRecorder
import com.foryouandme.researchkit.step.Step
import com.squareup.moshi.Moshi
import java.io.File

class LocationRecorderConfig(
    private val moshi: Moshi,
    private val minTime: Long = DEFAULT_MIN_TIME,
    private val minDistance: Float = DEFAULT_LOCATION_DISTANCE.toFloat(),
    private val usesRelativeCoordinates: Boolean = DEFAULT_USES_RELATIVE_COORDINATE,
) : RecorderConfig(LocationRecorder.LOCATION_IDENTIFIER) {

    override fun recorderForStep(step: Step, outputDirectory: File): Recorder =
        LocationRecorder(
            identifier,
            step,
            File(outputDirectory, LocationRecorder.LOCATION_IDENTIFIER),
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