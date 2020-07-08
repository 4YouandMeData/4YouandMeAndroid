package org.fouryouandme.core.data.api.wearable.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.wearable.Wearable

@JsonApi(type = "wearable")
data class WearableResponse(
    val pages: HasMany<PageResponse>? = null,
    val welcomePage: HasOne<PageResponse>? = null,
    val failurePage: HasOne<PageResponse>? = null,
    val successPage: HasOne<PageResponse>? = null
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