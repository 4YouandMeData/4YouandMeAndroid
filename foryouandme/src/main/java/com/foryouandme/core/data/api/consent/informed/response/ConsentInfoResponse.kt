package com.foryouandme.core.data.api.consent.informed.response

import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.core.data.api.common.response.QuestionResponse
import com.foryouandme.entity.consent.informed.ConsentInfo
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*

@JsonApi(type = "informed_consent")
data class ConsentInfoResponse(
    @field:Json(name = "minimum_required_correct_answers")
    val minimumAnswer: Int? = null,
    @field:Json(name = "questions")
    val questions: HasMany<QuestionResponse>? = null,
    @field:Json(name = "pages")
    val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page")
    val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page")
    val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page")
    val failurePage: HasOne<PageResponse>? = null
) : Resource() {

    fun toConsentInfo(document: ObjectDocument<ConsentInfoResponse>): ConsentInfo? {

        val questionsList =
            questions?.get(document)?.mapNotNull { it.toConsentQuestion(document) }

        val pageList = pages?.get(document)?.mapNotNull { it.toPage(document) }
        val welcomePage = welcomePage?.get(document)?.toPage(document)
        val successPage = successPage?.get(document)?.toPage(document)
        val failurePage = failurePage?.get(document)?.toPage(document)

        return when (null) {

            minimumAnswer, questionsList, pageList, welcomePage, successPage, failurePage -> null
            else ->
                ConsentInfo(
                    minimumAnswer = minimumAnswer,
                    questions = questionsList,
                    pages = pageList,
                    welcomePage = welcomePage,
                    successPage = successPage,
                    failurePage = failurePage
                )

        }
    }
}

