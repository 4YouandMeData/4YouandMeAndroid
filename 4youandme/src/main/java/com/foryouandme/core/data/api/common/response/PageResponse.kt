package com.foryouandme.core.data.api.common.response

import arrow.core.Either
import com.foryouandme.core.entity.page.Page
import com.foryouandme.core.ext.emptyOrBlankToNull
import com.squareup.moshi.Json
import moe.banana.jsonapi2.Document
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "page")
data class PageResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "link_1") val link1: HasOne<PageResponse>? = null,
    @field:Json(name = "link_1_label") val link1Label: String? = null,
    @field:Json(name = "link_2") val link2: HasOne<PageResponse>? = null,
    @field:Json(name = "link_2_label") val link2Label: String? = null,
    @field:Json(name = "external_link_label") val externalLinkLabel: String? = null,
    @field:Json(name = "external_link_url") val externalLinkUrl: String? = null,
    @field:Json(name = "special_link_label") val specialLinkLabel: String? = null,
    @field:Json(name = "special_link_data") val specialLinkValue: String? = null,
    @field:Json(name = "link_modal_label") val linkModalLabel: String? = null,
    @field:Json(name = "link_modal") val linkModalValue: HasOne<PageResponse>? = null
) : Resource() {

    suspend fun toPage(document: Document): Page? =
        Either.catch {

            Page(
                id,
                title!!,
                body!!,
                image,
                link1?.get(document)?.toPage(document),
                link1Label.emptyOrBlankToNull(),
                link2?.get(document)?.toPage(document),
                link2Label.emptyOrBlankToNull(),
                externalLinkLabel.emptyOrBlankToNull(),
                externalLinkUrl.emptyOrBlankToNull(),
                specialLinkLabel.emptyOrBlankToNull(),
                specialLinkValue.emptyOrBlankToNull(),
                linkModalLabel.emptyOrBlankToNull(),
                linkModalValue?.get(document)?.toPage(document)
            )

        }.orNull()

}