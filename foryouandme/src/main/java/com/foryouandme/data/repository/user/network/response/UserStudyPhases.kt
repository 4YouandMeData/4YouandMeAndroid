package com.foryouandme.data.repository.user.network.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "user_study_phase")
data class UserStudyPhaseResponse(
    @field:Json(name = "start_at") val start: String? = null,
    @field:Json(name = "end_at") val end: String? = null,
    @field:Json(name = "study_phase") val studyPhase: HasOne<StudyPhaseResponse>? = null
) : Resource()

@JsonApi(type = "study_phase")
data class StudyPhaseResponse(
    @field:Json(name = "name") val name: String? = null
) : Resource()