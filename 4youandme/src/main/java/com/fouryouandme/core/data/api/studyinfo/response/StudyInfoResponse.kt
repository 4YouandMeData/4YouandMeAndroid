package com.fouryouandme.core.data.api.studyinfo.response

import arrow.core.Either
import com.fouryouandme.core.data.api.common.response.PageResponse
import com.fouryouandme.core.entity.studyinfo.StudyInfo
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

    suspend fun toStudyInfo(document: ObjectDocument<StudyInfoResponse>): StudyInfo? =
        Either.catch {

            StudyInfo(
                informationPage?.get(document)?.toPage(document)!!,
                faqPage?.get(document)?.toPage(document)!!,
                rewardPage?.get(document)?.toPage(document)!!
            )

        }.orNull()

}