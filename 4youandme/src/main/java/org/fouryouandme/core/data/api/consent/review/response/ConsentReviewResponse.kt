package org.fouryouandme.core.data.api.consent.review.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.consent.review.ConsentReview

@JsonApi(type = "consent")
data class ConsentReviewResponse(

    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "disagree_modal_body") val disagreeModalBody: String? = null,
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null

) : Resource() {

    fun toConsentReview(document: ObjectDocument<ConsentReviewResponse>): Option<ConsentReview> =
        Option.fx {

            ConsentReview(
                !title.toOption(),
                !body.toOption(),
                !disagreeModalBody.toOption(),
                !pages?.get(document)
                    ?.mapNotNull { it.toPage(document).orNull() }
                    .toOption(),
                !welcomePage?.get(document)
                    .toOption()
                    .flatMap { it.toPage(document) }
            )

        }

}