package com.foryouandme.data.repository.study.network.response

import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.entity.studyinfo.StudyInfo
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.ObjectDocument
import moe.banana.jsonapi2.Resource

@JsonApi(type = "study_info")
data class StudyInfoResponse(
    @field:Json(name = "information_page") val informationPage: HasOne<PageResponse>? = null,
    @field:Json(name = "faq_page") val faqPage: HasOne<PageResponse>? = null,
    @field:Json(name = "reward_page") val rewardPage: HasOne<PageResponse>? = null
) : Resource() {

    fun toStudyInfo(document: ObjectDocument<StudyInfoResponse>): StudyInfo? {

        val information = informationPage?.get(document)?.toPage(document)
        val faq = faqPage?.get(document)?.toPage(document)
        val reward = rewardPage?.get(document)?.toPage(document)

        return when (null) {
            information, faq, reward -> null
            else -> StudyInfo(information, faq, reward)
        }

    }

}