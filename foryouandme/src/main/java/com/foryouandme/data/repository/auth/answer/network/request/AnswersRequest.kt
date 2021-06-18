package com.foryouandme.data.repository.auth.answer.network.request

import com.squareup.moshi.Json

data class AnswersRequest(
    @Json(name = "answer") val answer: AnswerDataRequest
)

data class AnswerDataRequest(
    @Json(name = "answer_text") val answerText: String,
    @Json(name = "batch_code") val batchCode: String,
    @Json(name = "possible_answer_id") val possibleAnswerId: String
)