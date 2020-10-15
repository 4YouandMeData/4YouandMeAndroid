package org.fouryouandme.core.data.api.survey.response

import arrow.core.Either
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.survey.SurveyQuestion
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset

@JsonApi(type = "question")
data class SurveyQuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "type") val questionType: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "min_date") val minDate: String? = null,
    @field:Json(name = "max_date") val maxDate: String? = null,
) : Resource() {

    suspend fun toSurveyQuestion(): SurveyQuestion? =
        Either.catch {
            when (questionType) {

                "SurveyQuestionDate" ->
                    SurveyQuestion.Date(
                        id,
                        text!!,
                        image,
                        Instant.parse(minDate).atZone(ZoneOffset.UTC),
                        Instant.parse(maxDate).atZone(ZoneOffset.UTC),
                    )
                else -> null

            }
        }.orNull()

}