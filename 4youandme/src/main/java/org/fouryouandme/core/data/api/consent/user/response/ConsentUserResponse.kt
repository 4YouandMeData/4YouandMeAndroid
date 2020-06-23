package org.fouryouandme.core.data.api.consent.user.response

import arrow.core.Option
import arrow.core.toOption
import com.squareup.moshi.Json
import moe.banana.jsonapi2.HasOne
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.ObjectDocument
import moe.banana.jsonapi2.Resource
import org.fouryouandme.core.data.api.common.response.PageResponse
import org.fouryouandme.core.entity.consent.user.ConsentUser

@JsonApi(type = "signature")
data class ConsentUserResponse(
    @field:Json(name = "success_page") val successPage: HasOne<PageResponse>? = null
) : Resource() {

    fun toConsentUser(document: ObjectDocument<ConsentUserResponse>): Option<ConsentUser> =
        successPage?.get(document)
            .toOption()
            .flatMap { it.toPage(document) }
            .map { ConsentUser(it) }
}