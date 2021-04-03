package com.foryouandme.data.repository.auth.integration.network.response

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

    fun toIntegration(document: ObjectDocument<IntegrationResponse>): Integration? {

        val pages = pages?.get(document)?.mapNotNull { it.toPage(document) }
        val welcomePage = welcomePage?.get(document)?.toPage(document)
        val successPage = successPage?.get(document)?.toPage(document)

        return when (null) {
            pages, welcomePage, successPage -> null
            else ->
                Integration(
                    pages,
                    welcomePage,
                    successPage
                )
        }
    }

}