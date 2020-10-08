package org.fouryouandme.core.data.api.optins.response

import arrow.core.Either
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

    suspend fun toOptIns(document: ObjectDocument<OptInsResponse>): OptIns? =
        Either.catch {

            OptIns(
                welcomePage?.get(document)?.toPage(document)!!,
                successPage?.get(document)?.toPage(document)!!,
                permissions?.get(document)
                    ?.toOptInsPermissions()
                    ?: emptyList()
            )

        }.orNull()
}