package com.foryouandme.researchkit.recorder.sensor.pedometer

import com.foryouandme.researchkit.recorder.sensor.RecorderData
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PedometerRecorderData(
    timeStamp: Long,
    val steps: Int,
    val distance: Float
) : RecorderData(timeStamp) {

    suspend fun toJson(moshi: Moshi): String =
        withContext(Dispatchers.IO) {
            moshi.adapter(PedometerRecorderDataJson::class.java)
                .toJson(PedometerRecorderDataJson.from(this@PedometerRecorderData))
        }

}

