package org.fouryouandme.core.data.api.auth.request

import com.squareup.moshi.Json

data class UserUpdateResponse(
    @Json(name = "user") val user: UserUpdateDataResponse
)

data class UserUpdateDataResponse(
    @Json(name = "custom_data") val customData: List<UserCustomDataRequest>
)

data class UserCustomDataRequest(
    @Json(name = "identifier") val identifier: String,
    @Json(name = "value") val value: String? = null,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "items") val items: List<UserCustomDataItemRequest>,
)

data class UserCustomDataItemRequest(
    @Json(name = "identifier") val identifier: String,
    @Json(name = "value") val value: String,
)
