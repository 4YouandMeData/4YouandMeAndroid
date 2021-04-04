package com.foryouandme.data.repository.auth.consent.network.response

import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
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

    fun toConsentReview(document: ObjectDocument<ConsentReviewResponse>): ConsentReview? {

        val pages = pages?.get(document)?.mapNotNull { it.toPage(document) }
        val welcomePage = welcomePage?.get(document)?.toPage(document)

        return when (null) {
            title, body, pagesSubtitle, disagreeModalBody,
            disagreeModalButton, pages, welcomePage -> null
            else ->
                ConsentReview(
                    title,
                    body,
                    pagesSubtitle,
                    disagreeModalBody,
                    disagreeModalButton,
                    pages,
                    welcomePage
                )

        }

    }

}