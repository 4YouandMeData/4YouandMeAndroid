package org.fouryouandme.core.data.api.wearable.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.wearable.Wearable

@JsonApi(type = "wearable")
data class WearableResponse(
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page") val failurePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null
) : Resource() {

    fun toWearable(document: ObjectDocument<WearableResponse>): Option<Wearable> =
        Option.fx {
            Wearable(
                !pages?.get(document)
                    ?.mapNotNull { it.toPage(document).orNull() }
                    .toOption(),
                !welcomePage?.get(document).toOption().flatMap { it.toPage(document) },
                !successPage?.get(document).toOption().flatMap { it.toPage(document) }
            )
        }

}