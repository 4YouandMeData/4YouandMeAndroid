package org.fouryouandme.researchkit.recorder.sensor.pedometer

import com.squareup.moshi.Json

internal data class PedometerRecorderDataJson(
    @Json(name = "time_stamp") val timeStamp: Long,
    @Json(name = "step") val steps: Int,
    @Json(name = "distance") val distance: Float
) {

    companion object {

        fun from(
            data: PedometerRecorderData
        ): PedometerRecorderDataJson =
            PedometerRecorderDataJson(
                data.timeStamp,
                data.steps,
                data.distance
            )

    }

}