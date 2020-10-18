package org.fouryouandme.core.data.api.task.request

import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson

data class SurveyUpdateRequest(
    @Json(name = "answers") val answers: List<AnswerUpdateRequest>
)

// TODO: fix this response to serialize child fields
open class AnswerUpdateRequest(
    @Json(name = "question_id") val questionId: String
) {

    class SingleAnswerUpdateRequest(
        questionId: String,
        @Json(name = "answer") val answer: String,
    ) : AnswerUpdateRequest(questionId)

    class SingleIntAnswerUpdateRequest(
        questionId: String,
        @Json(name = "answer") val answer: Int,
    ) : AnswerUpdateRequest(questionId)

    class MultipleAnswerUpdateRequest(
        questionId: String,
        @Json(name = "answer") val answers: List<String>,
    ) : AnswerUpdateRequest(questionId)

}

class AnswerUpdateRequestAdapter {

    @FromJson
    fun fromJson(json: AnswerUpdateRequest): AnswerUpdateRequest {
        return json
    }

    @ToJson
    fun toJson(value: List<AnswerUpdateRequest>): AnswerUpdateRequest {
        throw UnsupportedOperationException()
    }

}