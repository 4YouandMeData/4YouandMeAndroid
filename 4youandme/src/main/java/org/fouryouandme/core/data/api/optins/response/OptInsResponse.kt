package org.fouryouandme.core.data.api.optins.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.optins.OptIns

@JsonApi(type = "opt_in")
data class OptInsResponse(
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "permissions") val permissions: HasMany<OptInsPermissionResponse>? = null
) : Resource() {

    fun toOptIns(document: ObjectDocument<OptInsResponse>): Option<OptIns> =
        Option.fx {

            OptIns(
                !welcomePage.toOption().flatMap { it.get(document).toPage(document) },
                !successPage.toOption().flatMap { it.get(document).toPage(document) },
                permissions?.get(document)
                    .toOption()
                    .map { it.toOptInsPermissions() }
                    .getOrElse { emptyList() }
            )

        }
}