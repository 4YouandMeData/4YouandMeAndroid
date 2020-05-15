package org.fouryouandme.core.data.api.auth.response

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "email") val email: String? = null
)
