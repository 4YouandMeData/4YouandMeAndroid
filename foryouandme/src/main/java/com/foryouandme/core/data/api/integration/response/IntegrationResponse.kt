package com.foryouandme.core.data.api.integration.response

import arrow.core.Either
import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.entity.integration.Integration
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*

@JsonApi(type = "integration")
data class IntegrationResponse(
    @field:Json(name = "pages") val pages: HasMany<PageResponse>? = null,
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "failure_page") val failurePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null
) : Resource() {

    suspend fun toIntegration(document: ObjectDocument<IntegrationResponse>): Integration? =
        Either.catch {
            Integration(
                pages?.get(document)?.mapNotNull { it.toPage(document) }!!,
                welcomePage?.get(document)?.toPage(document)!!,
                successPage?.get(document)?.toPage(document)!!
            )
        }.orNull()

}