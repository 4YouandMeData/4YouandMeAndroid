package org.fouryouandme.core.data.api.optins.response

import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "opt_in")
data class OptInsResponse(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "permissions") val permissions: HasOne<OptInsPermissionResponse>
) : Resource()