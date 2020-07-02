package org.fouryouandme.core.data.api.optins.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.ObjectDocument
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.entity.optins.OptIns

@JsonApi(type = "opt_in")
data class OptInsResponse(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String,
    @field:Json(name = "permissions") val permissions: HasOne<OptInsPermissionResponse>
) : Resource() {

    fun toOptIns(document: ObjectDocument<OptInsResponse>): Option<OptIns> =
        Option.fx {

            OptIns(
                !title.toOption(),
                !description.toOption(),
                !permissions.get(document).toOptInsPermission()
            )

        }
}