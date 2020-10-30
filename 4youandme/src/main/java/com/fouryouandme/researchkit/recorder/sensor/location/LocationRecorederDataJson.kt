package com.fouryouandme.researchkit.recorder.sensor.location

import com.squareup.moshi.Json

internal data class LocationRecorderDataJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "coordinate") val coordinate: LocationRecorderCoordinateJson,
    @Json(name = "relative_coordinate") val relativeCoordinate: LocationRecorderCoordinateJson,
    @Json(name = "accuracy") val accuracy: Float,
    @Json(name = "speed") val speed: Float,
    @Json(name = "altitude") val altitude: Double,
    @Json(name = "course") val course: Float,
    @Json(name = "distance") val distance: Double
) {

    companion object {

        fun from(
            data: LocationRecorderData
        ): LocationRecorderDataJson =
            LocationRecorderDataJson(
                data.timeStamp,
                LocationRecorderCoordinateJson.from(data.coordinate),
                LocationRecorderCoordinateJson.from(data.relativeCoordinate),
                data.accuracy,
                data.speed,
                data.altitude,
                data.course,
                data.distance
            )

    }

}

internal data class LocationRecorderCoordinateJson(
    @Json(name = "latitude") val latitude: Double,
    @Json(name = "longitude") val longitude: Double
) {

    companion object {

        fun from(
            data: LocationRecorderCoordinate
        ): LocationRecorderCoordinateJson =
            LocationRecorderCoordinateJson(data.latitude, data.longitude)

    }

}