package com.foryouandme.data.repository.auth.screening.network.response


import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.data.repository.auth.answer.network.response.QuestionResponse
import com.foryouandme.entity.screening.Screening
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*

@JsonApi(type = "screening")
data class ScreeningResponse(
    @field:Json(name = "minimum_required_correct_answers")
    val minimumAnswer: Int? = null,
    @field:Json(name = "screening_questions")
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

    fun toScreening(document: ObjectDocument<ScreeningResponse>): Screening? {

        val welcomePage = welcomePage?.get(document)?.toPage(document)
        val successPage = successPage?.get(document)?.toPage(document)
        val failurePage = failurePage?.get(document)?.toPage(document)

        return when (null) {
            minimumAnswer, welcomePage, successPage, failurePage -> null
            else ->
                Screening(
                    minimumAnswer,
                    questions?.get(document)
                        ?.mapNotNull { it.toScreeningQuestion(document) } ?: emptyList(),
                    pages?.get(document)
                        ?.mapNotNull { it.toPage(document) } ?: emptyList(),
                    welcomePage,
                    successPage,
                    failurePage
                )
        }

    }
}