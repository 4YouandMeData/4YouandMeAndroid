package com.fouryouandme.core.data.api.consent.user.request

import com.squareup.moshi.Json

data class CreateUserConsentRequest(
    @Json(name = "agree") val agree: Boolean,
    @Json(name = "new_email") val email: String
)