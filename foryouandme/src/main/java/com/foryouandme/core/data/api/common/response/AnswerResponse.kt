package com.foryouandme.core.data.api.common.response

import arrow.core.Either
import com.foryouandme.core.entity.consent.informed.ConsentInfoAnswer
import com.foryouandme.core.entity.screening.ScreeningAnswer
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "possible_answer")
data class AnswerResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "correct") val correct: Boolean? = null
) : Resource() {

    suspend fun toScreeningAnswer(): ScreeningAnswer? =
        Either.catch {
            ScreeningAnswer(id, text!!, correct!!)
        }.orNull()

    suspend fun toConsentAnswer(): ConsentInfoAnswer? =
        Either.catch {
            ConsentInfoAnswer(
                id,
                text!!,
                correct!!
            )
        }.orNull()
}