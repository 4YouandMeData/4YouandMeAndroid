package com.foryouandme.data.repository.auth.consent.network.response

import com.foryouandme.data.repository.auth.answer.network.response.PageResponse
import com.foryouandme.entity.optins.OptIns
import com.squareup.moshi.Json
import moe.banana.jsonapi2.*

@JsonApi(type = "opt_in")
data class OptInsResponse(
    @field:Json(name = "welcome_page") val welcomePage: HasOne<PageResponse>? = null,
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null,
    @field:Json(name = "permissions") val permissions: HasMany<OptInsPermissionResponse>? = null
) : Resource() {

    fun toOptIns(document: ObjectDocument<OptInsResponse>): OptIns? {

        val welcomePage = welcomePage?.get(document)?.toPage(document)
        val successPage = successPage?.get(document)?.toPage(document)
        val permissions = permissions?.get(document)?.toOptInsPermissions() ?: emptyList()

        return when (null) {
            welcomePage, successPage -> null
            else ->
                OptIns(
                    welcomePage,
                    successPage,
                    permissions
                )
        }
    }

}