package org.fouryouandme.core.data.api.survey.response

import arrow.core.Either
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.survey.SurveyTarget

data class SurveyQuestionTargetResponse(
    @Json(name = "min") val min: Int? = null,
    @Json(name = "max") val max: Int? = null,
    @Json(name = "answer_id") val answerId: String? = null,
    @Json(name = "question_id") val questionId: String? = null
) {

    suspend fun toRange(): SurveyTarget.Range? =
        Either.catch {

            SurveyTarget.Range(
                min,
                max,
                questionId!!
            )

        }.orNull()

    suspend fun toAnswer(): SurveyTarget.Answer? =
        Either.catch {

            SurveyTarget.Answer(
                answerId!!,
                questionId!!
            )

        }.orNull()

}