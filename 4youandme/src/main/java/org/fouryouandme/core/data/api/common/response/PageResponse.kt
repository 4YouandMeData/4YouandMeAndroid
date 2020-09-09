package org.fouryouandme.core.data.api.common.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.Document
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.page.Page
import org.fouryouandme.core.ext.emptyOrBlankToNone

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
    @field:Json(name = "link_modal") val linkModalValue : HasOne<PageResponse>? = null
) : Resource() {

    fun toPage(document: Document): Option<Page> =
        Option.fx {

            Page(
                id,
                !title.toOption(),
                !body.toOption(),
                image.toOption(),
                link1?.get(document).toOption().flatMap { it.toPage(document) },
                link1Label.emptyOrBlankToNone(),
                link2?.get(document).toOption().flatMap { it.toPage(document) },
                link2Label.emptyOrBlankToNone(),
                externalLinkLabel.emptyOrBlankToNone(),
                externalLinkUrl.emptyOrBlankToNone(),
                specialLinkLabel.emptyOrBlankToNone(),
                specialLinkValue.emptyOrBlankToNone(),
                linkModalLabel.emptyOrBlankToNone(),
                linkModalValue?.get(document).toOption().flatMap { it.toPage(document) }
            )

        }

}