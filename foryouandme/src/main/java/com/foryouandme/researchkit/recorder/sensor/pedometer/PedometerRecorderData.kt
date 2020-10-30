package com.foryouandme.researchkit.recorder.sensor.pedometer

import com.foryouandme.core.ext.evalOnIO
import com.foryouandme.researchkit.recorder.sensor.RecorderData
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

