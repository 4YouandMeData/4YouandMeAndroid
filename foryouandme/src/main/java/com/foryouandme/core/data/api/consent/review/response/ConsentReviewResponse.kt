package com.foryouandme.core.data.api.consent.review.response

import arrow.core.Either
import com.foryouandme.core.data.api.common.response.PageResponse
import com.foryouandme.entity.consent.review.ConsentReview
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*

@JsonApi(type = "consent")
data class ConsentReviewResponse(

    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "pages_subtitle") val pagesSubtitle: String? = null,
    @field:Json(name = "disagree_modal_body") val disagreeModalBody: String? = null,
    @field:Json(name = "disagree_modal_button") val disagreeModalButton: String? = null,
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null

) : Resource() {

    suspend fun toConsentReview(document: ObjectDocument<ConsentReviewResponse>): ConsentReview? =
        Either.catch {

            ConsentReview(
                title!!,
                body!!,
                pagesSubtitle!!,
                disagreeModalBody!!,
                disagreeModalButton!!,
                pages?.get(document)
                    ?.mapNotNull { it.toPage(document) }!!,
                welcomePage?.get(document)?.toPage(document)!!
            )

        }.orNull()

}