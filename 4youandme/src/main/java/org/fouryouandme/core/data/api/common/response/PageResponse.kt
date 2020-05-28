package org.fouryouandme.core.data.api.common.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "page")
data class PageResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "link_1_label") val link1Label: String? = null,
    @field:Json(name = "link_2_label") val link2Label: String? = null,
    @field:Json(name = "external_link_label") val externalLinkLabel: String? = null,
    @field:Json(name = "external_link_url") val externalLinkUrl: String? = null
): Resource()