package com.foryouandme.core.data.api.optins.response

import android.Manifest
import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import com.foryouandme.core.entity.optins.OptInsPermission
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

    fun toOptInsPermission(): Option<OptInsPermission> =
        Option.fx {

            OptInsPermission(
                id,
                image.toOption(),
                !title.toOption(),
                !body.toOption(),
                !position.toOption(),
                !agreeText.toOption(),
                !disagreeText.toOption(),
                systemPermissions.toOption()
                    .getOrElse { emptyList() }
                    .mapNotNull { mapPermission(it) },
                mandatory.toOption().getOrElse { false },
                mandatoryDescription.toOption()
            )

        }
}

fun List<OptInsPermissionResponse>.toOptInsPermissions(): List<OptInsPermission> =
    mapNotNull { it.toOptInsPermission().orNull() }

private fun mapPermission(permission: String): String? =
    when (permission) {
        "location" -> Manifest.permission.ACCESS_FINE_LOCATION
        else -> null
    }