package com.foryouandme.data.repository.survey.network.response

import com.foryouandme.entity.survey.SurveyAnswer
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "possible_answer")
data class SurveyAnswerResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "correct") val correct: Boolean? = null,
) : Resource() {

    fun toSurveyAnswer(): SurveyAnswer? =
        when (null) {
            text, correct -> null
            else ->
                SurveyAnswer(
                    id,
                    text,
                    correct
                )
        }

}