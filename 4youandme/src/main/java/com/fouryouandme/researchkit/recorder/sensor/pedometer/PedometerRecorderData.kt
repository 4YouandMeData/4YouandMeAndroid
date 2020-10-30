package com.fouryouandme.researchkit.recorder.sensor.pedometer

import com.fouryouandme.core.ext.evalOnIO
import com.fouryouandme.researchkit.recorder.sensor.RecorderData
import com.squareup.moshi.Moshi

class PedometerRecorderData(
    timeStamp: Long,
    val steps: Int,
    val distance: Float
) : RecorderData(timeStamp) {

    suspend fun toJson(moshi: Moshi): String =
        evalOnIO {
            moshi.adapter(PedometerRecorderDataJson::class.java)
                .toJson(PedometerRecorderDataJson.from(this))
        }

}

