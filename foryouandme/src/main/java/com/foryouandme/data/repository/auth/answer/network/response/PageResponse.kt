package com.foryouandme.data.repository.auth.answer.network.response

import com.foryouandme.core.ext.emptyOrBlankToNull
import com.foryouandme.entity.page.Page
import com.foryouandme.entity.page.PageRef
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

    fun toPage(document: Document): Page? =
        when (null) {

            title, body -> null
            else ->
                Page(
                    id,
                    title,
                    body,
                    image,
                    link1?.get(document)?.toPageRef(),
                    link1Label.emptyOrBlankToNull(),
                    link2?.get(document)?.toPageRef(),
                    link2Label.emptyOrBlankToNull(),
                    externalLinkLabel.emptyOrBlankToNull(),
                    externalLinkUrl.emptyOrBlankToNull(),
                    specialLinkLabel.emptyOrBlankToNull(),
                    specialLinkValue.emptyOrBlankToNull(),
                    linkModalLabel.emptyOrBlankToNull(),
                    linkModalValue?.get(document)?.toPageRef()
                )

        }

    private fun toPageRef(): PageRef = PageRef(id)

}