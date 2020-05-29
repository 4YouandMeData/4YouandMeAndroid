package org.fouryouandme.core.data.api.common.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.page.Page

@JsonApi(type = "page")
data class PageResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "link_1_label") val link1Label: String? = null,
    @field:Json(name = "link_2_label") val link2Label: String? = null,
    @field:Json(name = "external_link_label") val externalLinkLabel: String? = null,
    @field:Json(name = "external_link_url") val externalLinkUrl: String? = null
): Resource() {

    fun toPage(): Option<Page> =
        Option.fx {

            Page(
                !title.toOption(),
                !body.toOption(),
                !image.toOption(),
                !link1Label.toOption(),
                !link2Label.toOption(),
                !externalLinkLabel.toOption(),
                !externalLinkUrl.toOption()
            )

        }

}