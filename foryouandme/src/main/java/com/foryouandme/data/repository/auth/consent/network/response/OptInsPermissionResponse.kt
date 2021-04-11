package com.foryouandme.data.repository.auth.consent.network.response

import com.foryouandme.core.cases.permission.Permission
import com.foryouandme.entity.optins.OptInsPermission
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "permission")
data class OptInsPermissionResponse(
    @field:Json(name = "image") val image: String? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "position") val position: Int? = null,
    @field:Json(name = "agree_text") val agreeText: String? = null,
    @field:Json(name = "disagree_text") val disagreeText: String? = null,
    @field:Json(name = "system_permissions") val systemPermissions: List<String>? = null,
    @field:Json(name = "mandatory") val mandatory: Boolean? = null,
    @field:Json(name = "mandatory_description") val mandatoryDescription: String? = null
) : Resource() {

    fun toOptInsPermission(): OptInsPermission? =
        when (null) {
            title, body, position, agreeText, disagreeText -> null
            else ->
                OptInsPermission(
                    id,
                    image,
                    title,
                    body,
                    position,
                    agreeText,
                    disagreeText,
                    systemPermissions?.mapNotNull { mapPermission(it) } ?: emptyList(),
                    mandatory ?: false,
                    mandatoryDescription
                )

        }
}

fun List<OptInsPermissionResponse>.toOptInsPermissions(): List<OptInsPermission> =
    mapNotNull { it.toOptInsPermission() }

private fun mapPermission(permission: String): Permission? =
    when (permission) {
        "location" -> Permission.Location
        else -> null
    }