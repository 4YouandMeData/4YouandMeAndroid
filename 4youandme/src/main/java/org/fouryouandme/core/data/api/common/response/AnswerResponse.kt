package org.fouryouandme.core.data.api.common.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.consent.informed.ConsentInfoAnswer
import org.fouryouandme.core.entity.screening.ScreeningAnswer

@JsonApi(type = "possible_answer")
data class AnswerResponse(
    @field:Json(name = "text") val text: String? = null,
    @field:Json(name = "correct") val correct: Boolean? = null
) : Resource() {

    fun toScreeningAnswer(): Option<ScreeningAnswer> =
        Option.fx {
            ScreeningAnswer(id, !text.toOption(), !correct.toOption())
        }

    fun toConsentAnswer(): Option<ConsentInfoAnswer> =
        Option.fx {
            ConsentInfoAnswer(
                id,
                !text.toOption(),
                !correct.toOption()
            )
        }
}