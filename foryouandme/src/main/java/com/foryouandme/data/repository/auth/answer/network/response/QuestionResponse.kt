package com.foryouandme.data.repository.auth.answer.network.response

import com.foryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import com.foryouandme.data.repository.auth.screening.network.response.ScreeningResponse
import com.foryouandme.entity.consent.informed.ConsentInfoQuestion
import com.foryouandme.entity.screening.ScreeningQuestion
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.ObjectDocument
import moe.banana.jsonapi2.Resource

@JsonApi(type = "question")
data class QuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "possible_answers") val answer: HasMany<AnswerResponse>? = null
) : Resource() {

    fun toScreeningQuestion(
        document: ObjectDocument<ScreeningResponse>
    ): ScreeningQuestion? {

        val answer1 = answer?.get(document)?.getOrNull(0)?.toScreeningAnswer()
        val answer2 = answer?.get(document)?.getOrNull(1)?.toScreeningAnswer()

        return when (null) {

            text, answer1, answer2 -> null
            else -> ScreeningQuestion(
                id,
                text,
                answer1,
                answer2
            )

        }
    }

    fun toConsentQuestion(
        document: ObjectDocument<ConsentInfoResponse>
    ): ConsentInfoQuestion? {

        val answerList = answer?.get(document)?.mapNotNull { it.toConsentAnswer() }

        return when (null) {
            text, answerList -> null
            else -> ConsentInfoQuestion(
                id,
                text,
                answerList
            )
        }

    }
}