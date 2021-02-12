package com.foryouandme.core.data.api.survey.response

import arrow.core.Either
import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.entity.survey.Survey
import com.foryouandme.entity.survey.SurveyBlock
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "survey")
data class SurveyResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "active") val active: Boolean? = null,
    @field:Json(name = "color") val color: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "survey_blocks") val surveyBlocks: HasMany<SurveyBlockResponse>? = null
) : Resource() {

    suspend fun toSurvey(): Survey? =
        Either.catch {

            Survey(
                id,
                title,
                description,
                active,
                color,
                image,
                surveyBlocks?.get(document)
                    ?.mapNotNull { it.toSurveyBlock() } ?: emptyList()
            )

        }.orNull()
}

@JsonApi(type = "survey_block")
data class SurveyBlockResponse(
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "intro_page") val introPage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "questions") val questions: HasMany<SurveyQuestionResponse>? = null,
) : Resource() {

    suspend fun toSurveyBlock(): SurveyBlock? =
        Either.catch {

            val questions =
                questions
                    ?.get(document)
                    ?.mapNotNull { it.toSurveyQuestion() }
                    ?.let { if (it.isEmpty()) null else it }

            SurveyBlock(
                id,
                pages?.get(document)?.mapNotNull { it.toPage(document) } ?: emptyList(),
                introPage?.get(document)?.toPage(document),
                successPage?.get(document)?.toPage(document),
                questions!!
            )

        }.orNull()

}