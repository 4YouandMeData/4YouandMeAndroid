package com.foryouandme.core.data.api.common.response

import com.foryouandme.entity.consent.informed.ConsentInfoAnswer
import com.foryouandme.entity.screening.ScreeningAnswer
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "possible_answer")
data class AnswerResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "correct") val correct: Boolean? = null
) : Resource() {

    fun toScreeningAnswer(): ScreeningAnswer? =
        when (null) {
            text, correct -> null
            else -> ScreeningAnswer(id, text, correct)
        }


    fun toConsentAnswer(): ConsentInfoAnswer? =
        when (null) {
            text, correct -> null
            else -> ConsentInfoAnswer(
                id,
                text,
                correct
            )
        }
}