package org.fouryouandme.researchkit.recorder.sensor.pedometer

import com.squareup.moshi.Moshi
import org.fouryouandme.core.ext.evalOnIO
import org.fouryouandme.researchkit.recorder.sensor.RecorderData

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

