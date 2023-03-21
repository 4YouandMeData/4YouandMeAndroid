package com.foryouandme.data.datasource.network.response

import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.entity.phase.StudyPhase
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "study_phase")
data class StudyPhaseResponse(
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "faq_page") val faqPage: HasOne<PageResponse>? = null,
) : Resource() {

    fun toStudyPhase(): StudyPhase? =
        when (null) {
            name -> null
            else -> StudyPhase(
                id = id,
                name = name,
                faq = faqPage?.get(document)?.toPage(document)
            )
        }

}