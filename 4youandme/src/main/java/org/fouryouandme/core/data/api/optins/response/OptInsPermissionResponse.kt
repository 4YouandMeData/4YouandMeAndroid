package org.fouryouandme.core.data.api.optins.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "permission")
data class OptInsPermissionResponse(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "body") val body: String,
    @field:Json(name = "position") val position: Int,
    @field:Json(name = "agree_text") val agreeText: String,
    @field:Json(name = "disagree_text") val disagreeText: String,
    @field:Json(name = "system_permissions") val systemPermissions: List<String>
) : Resource()