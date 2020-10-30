package com.foryouandme.core.data.api.screening.response


import arrow.core.Either
import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.core.data.api.common.response.QuestionResponse
import com.foryouandme.core.entity.screening.Screening
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

    suspend fun toScreening(document: ObjectDocument<ScreeningResponse>): Screening? =
        Either.catch {

            Screening(
                minimumAnswer!!,
                questions?.get(document)
                    ?.mapNotNull { it.toScreeningQuestion(document) } ?: emptyList(),
                pages?.get(document)
                    ?.mapNotNull { it.toPage(document) } ?: emptyList(),
                welcomePage?.get(document)?.toPage(document)!!,
                successPage?.get(document)?.toPage(document)!!,
                failurePage?.get(document)?.toPage(document)!!
            )

        }.orNull()
}