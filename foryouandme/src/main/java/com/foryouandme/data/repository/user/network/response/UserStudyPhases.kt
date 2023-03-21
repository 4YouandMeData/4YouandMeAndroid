package com.foryouandme.data.repository.user.network.response

import com.foryouandme.data.datasource.network.response.StudyPhaseResponse
import com.foryouandme.entity.phase.UserStudyPhase
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.threeten.bp.ZonedDateTime

@JsonApi(type = "user_study_phase")
data class UserStudyPhaseResponse(
    @field:Json(name = "start_at") val start: String? = null,
    @field:Json(name = "end_at") val end: String? = null,
    @field:Json(name = "study_phase") val studyPhase: HasOne<StudyPhaseResponse>? = null
) : Resource() {

    fun toUserStudyPhase(): UserStudyPhase {

        val startDate = ZonedDateTime.parse(start!!)
        val endDate = if (end != null) ZonedDateTime.parse(end) else null

        return UserStudyPhase(
            id = id,
            start = startDate,
            end = endDate,
            phase = studyPhase!!.get(document).toStudyPhase()!!
        )
    }
}