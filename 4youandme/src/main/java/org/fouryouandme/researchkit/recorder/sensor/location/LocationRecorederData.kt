package org.fouryouandme.researchkit.recorder.sensor.location

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.sensor.RecorderData

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

