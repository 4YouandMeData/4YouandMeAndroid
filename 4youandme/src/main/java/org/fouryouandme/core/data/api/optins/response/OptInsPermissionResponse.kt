package org.fouryouandme.core.data.api.optins.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.optins.OptInsPermission

@JsonApi(type = "permission")
data class OptInsPermissionResponse(
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "body") val body: String? = null,
    @field:Json(name = "position") val position: Int? = null,
    @field:Json(name = "agree_text") val agreeText: String? = null,
    @field:Json(name = "disagree_text") val disagreeText: String? = null,
    @field:Json(name = "system_permissions") val systemPermissions: List<String>? = null
) : Resource() {

    fun toOptInsPermission(): Option<OptInsPermission> =
        Option.fx {

            OptInsPermission(
                id,
                !title.toOption(),
                !body.toOption(),
                !position.toOption(),
                !agreeText.toOption(),
                !disagreeText.toOption(),
                systemPermissions.toOption().getOrElse { emptyList() }
            )

        }
}

fun List<OptInsPermissionResponse>.toOptInsPermissions(): List<OptInsPermission> =
    mapNotNull { it.toOptInsPermission().orNull() }