package com.foryouandme.data.repository.phase.network.request

import com.squareup.moshi.Json
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

data class CloseUserStudyPhaseRequest(
    @Json(name = "user_study_phase") val studyPhase: UserStudyPhaseEndRequest
) {

    companion object {

        fun build(): CloseUserStudyPhaseRequest =
            CloseUserStudyPhaseRequest(
                studyPhase =
                UserStudyPhaseEndRequest(
                    end =
                    ZonedDateTime.now()
                        .withZoneSameInstant(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                )
            )

    }

}

data class UserStudyPhaseEndRequest(
    @Json(name = "end_at") val end: String
)
