package com.foryouandme.researchkit.recorder.sensor.location

import com.foryouandme.researchkit.recorder.sensor.RecorderData
import com.squareup.moshi.Moshi

class LocationRecorderData(
    timeStamp: Long,
    val coordinate: LocationRecorderCoordinate,
    val relativeCoordinate: LocationRecorderCoordinate,
    val accuracy: Float,
    val speed: Float,
    val altitude: Double,
    val course: Float,
    val distance: Double

) : RecorderData(timeStamp) {

    fun toJson(moshi: Moshi): String =
        moshi.adapter(LocationRecorderDataJson::class.java)
            .toJson(LocationRecorderDataJson.from(this))
}

data class LocationRecorderCoordinate(
    val latitude: Double,
    val longitude: Double
)

