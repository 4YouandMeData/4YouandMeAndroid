package org.fouryouandme.core.data.api.screening.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.common.response.PageResponse

@JsonApi(type = "screening")
data class ScreeningResponse(
    @field:Json(name = "screening_questions")
    val questions: HasMany<ScreeningQuestionResponse>? = null,
    @field:Json(name = "pages")
    val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page")
    val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page")
    val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page")
    val failurePage: HasOne<PageResponse>? = null
) : Resource()

@JsonApi(type = "question")
data class ScreeningQuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "possible_answers") val answer: HasMany<ScreeningAnswerResponse>? = null
) : Resource()

@JsonApi(type = "possible_answer")
data class ScreeningAnswerResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "correct") val correct: Boolean? = null
) : Resource()