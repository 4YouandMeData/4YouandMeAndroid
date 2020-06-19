package org.fouryouandme.core.data.api.consent.informed.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.data.api.common.response.QuestionResponse
import org.fouryouandme.core.entity.consent.informed.ConsentInfo

@JsonApi(type = "informed_consent")
data class ConsentInfoResponse(
    @field:Json(name = "questions")
    val questions: HasMany<QuestionResponse>? = null,
    @field:Json(name = "pages")
    val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page")
    val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page")
    val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page")
    val failurePage: HasOne<PageResponse>? = null
) : Resource() {

    fun toConsentInfo(document: ObjectDocument<ConsentInfoResponse>): Option<ConsentInfo> =
        Option.fx {

            ConsentInfo(
                !questions?.get(document)
                    ?.mapNotNull { it.toConsentQuestion(document).orNull() }
                    .toOption(),
                !pages?.get(document)
                    ?.mapNotNull { it.toPage(document).orNull() }
                    .toOption(),
                !welcomePage?.get(document)
                    .toOption()
                    .flatMap { it.toPage(document) },
                !successPage?.get(document)
                    .toOption()
                    .flatMap { it.toPage(document) },
                !failurePage?.get(document)
                    .toOption()
                    .flatMap { it.toPage(document) }
            )

        }
}

