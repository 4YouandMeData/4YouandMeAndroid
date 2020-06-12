package org.fouryouandme.core.data.api.common.response

import arrow.core.Nel
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasMany
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.ObjectDocument
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.consent.response.ConsentResponse
import org.fouryouandme.core.data.api.screening.response.ScreeningResponse
import org.fouryouandme.core.entity.consent.ConsentQuestion
import org.fouryouandme.core.entity.screening.ScreeningQuestion

@JsonApi(type = "question")
data class QuestionResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "possible_answers") val answer: HasMany<AnswerResponse>? = null
) : Resource() {

    fun toScreeningQuestion(
        document: ObjectDocument<ScreeningResponse>
    ): Option<ScreeningQuestion> =
        Option.fx {
            ScreeningQuestion(
                id,
                !text.toOption(),
                !answer?.get(document)
                    ?.getOrNull(0)
                    .toOption()
                    .flatMap { it.toScreeningAnswer() },
                !answer?.get(document)
                    ?.getOrNull(1)
                    .toOption()
                    .flatMap { it.toScreeningAnswer() }
            )
        }

    fun toConsentQuestion(
        document: ObjectDocument<ConsentResponse>
    ): Option<ConsentQuestion> =
        Option.fx {
            ConsentQuestion(
                id,
                !text.toOption(),
                !answer?.get(document)
                    ?.mapNotNull { it.toConsentAnswer().orNull() }
                    .toOption()
                    .flatMap { Nel.fromList(it) }
            )
        }
}