package org.fouryouandme.core.data.api.task.request

import com.squareup.moshi.Json

data class SurveyUpdateRequest(
    @Json(name = "answers") val answers: List<AnswerUpdateRequest>
)

data class AnswerUpdateRequest(
    @Json(name = "question_id") val questionId: String,
    @Json(name = "answer") val answer: Any,
)


