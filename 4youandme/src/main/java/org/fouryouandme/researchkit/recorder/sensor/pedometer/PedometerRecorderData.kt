package org.fouryouandme.researchkit.recorder.sensor.pedometer

import com.squareup.moshi.Moshi
import org.fouryouandme.researchkit.recorder.sensor.RecorderData

class PedometerRecorderData(
    timeStamp: Long,
    val steps: Int,
    val distance: Float
) : RecorderData(timeStamp) {

    fun toJson(moshi: Moshi): String =
        moshi.adapter(PedometerRecorderDataJson::class.java)
            .toJson(PedometerRecorderDataJson.from(this))

}

