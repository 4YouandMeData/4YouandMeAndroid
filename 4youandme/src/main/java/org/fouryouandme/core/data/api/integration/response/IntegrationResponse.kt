package org.fouryouandme.core.data.api.integration.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.integration.Integration

@JsonApi(type = "integration")
data class IntegrationResponse(
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page") val failurePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null
) : Resource() {

    fun toIntegration(document: ObjectDocument<IntegrationResponse>): Option<Integration> =
        Option.fx {
            Integration(
                !pages?.get(document)
                    ?.mapNotNull { it.toPage(document).orNull() }
                    .toOption(),
                !welcomePage?.get(document).toOption().flatMap { it.toPage(document) },
                !successPage?.get(document).toOption().flatMap { it.toPage(document) }
            )
        }

}