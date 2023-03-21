package com.foryouandme.data.repository.study.network.response

import com.foryouandme.data.datasource.network.response.StudyPhaseResponse
import com.foryouandme.entity.study.Study
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "study")
data class StudyResponse(
    @field:Json(name = "study_phases") val phases: HasMany<StudyPhaseResponse>? = null
) : Resource() {

    fun toStudy(): Study =
        Study(
            phases = phases?.get(document)?.mapNotNull { it.toStudyPhase() } ?: emptyList()
        )

}