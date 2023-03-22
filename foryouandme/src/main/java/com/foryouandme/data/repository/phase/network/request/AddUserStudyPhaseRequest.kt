package com.foryouandme.data.repository.phase.network.request

import com.squareup.moshi.Json
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

data class AddUserStudyPhaseRequest(
    @Json(name = "user_study_phase") val studyPhase: UserStudyPhaseAddRequest
) {

    companion object {

        fun build(): AddUserStudyPhaseRequest =
            AddUserStudyPhaseRequest(
                studyPhase =
                UserStudyPhaseAddRequest(
                    start =
                    ZonedDateTime.now()
                        .withZoneSameInstant(ZoneOffset.UTC)
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                    customData = UserStudyPhaseAddCustomDataRequest()
                )
            )

    }

}

data class UserStudyPhaseAddRequest(
    @Json(name = "start_at") val start: String,
    @Json(name = "custom_data") val customData: UserStudyPhaseAddCustomDataRequest
)

class UserStudyPhaseAddCustomDataRequest
