package org.fouryouandme.core.data.api.consent.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.data.api.common.response.QuestionResponse
import org.fouryouandme.core.entity.consent.Consent

@JsonApi(type = "informed_consent")
data class ConsentResponse(
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

    fun toConsent(document: ObjectDocument<ConsentResponse>): Option<Consent> =
        Option.fx {

            Consent(
                !questions?.get(document)
                    ?.mapNotNull { it.toConsentQuestion(document).orNull() }
                    .toOption(),
                !pages?.get(document)
                    ?.mapNotNull { it.toPage().orNull() }
                    .toOption(),
                !welcomePage?.get(document)
                    .toOption()
                    .flatMap { it.toPage() },
                !successPage?.get(document)
                    .toOption()
                    .flatMap { it.toPage() },
                !failurePage?.get(document)
                    .toOption()
                    .flatMap { it.toPage() }
            )

        }
}

