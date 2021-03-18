package com.foryouandme.data.repository.survey.network.response

import com.foryouandme.entity.survey.SurveyTarget
import com.squareup.moshi.Json

data class SurveyQuestionTargetResponse(
    @Json(name = "min") val min: Int? = null,
    @Json(name = "max") val max: Int? = null,
    @Json(name = "answer_id") val answerId: String? = null,
    @Json(name = "question_id") val questionId: String? = null
) {

    fun toRange(): SurveyTarget.Range? =
        when (null) {
            questionId -> null
            else ->
                SurveyTarget.Range(
                    min,
                    max,
                    questionId
                )
        }

    fun toAnswer(): SurveyTarget.Answer? =
        when (null) {
            answerId, questionId -> null
            else ->
                SurveyTarget.Answer(
                    answerId,
                    questionId
                )
        }

}