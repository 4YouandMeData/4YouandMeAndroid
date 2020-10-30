package com.fouryouandme.core.data.api.common.response

import arrow.core.Either
import arrow.core.Nel
import com.fouryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import com.fouryouandme.core.data.api.screening.response.ScreeningResponse
import com.fouryouandme.core.entity.consent.informed.ConsentInfoQuestion
import com.fouryouandme.core.entity.screening.ScreeningQuestion
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

    suspend fun toScreeningQuestion(
        document: ObjectDocument<ScreeningResponse>
    ): ScreeningQuestion? =
        Either.catch {

            ScreeningQuestion(
                id,
                text!!,
                answer?.get(document)?.getOrNull(0)?.toScreeningAnswer()!!,
                answer.get(document)?.getOrNull(1)?.toScreeningAnswer()!!
            )

        }.orNull()

    suspend fun toConsentQuestion(
        document: ObjectDocument<ConsentInfoResponse>
    ): ConsentInfoQuestion? =
        Either.catch {
            ConsentInfoQuestion(
                id,
                text!!,
                answer?.get(document)
                    ?.mapNotNull { it.toConsentAnswer() }
                    ?.let { Nel.fromListUnsafe(it) }!!
            )
        }.orNull()
}