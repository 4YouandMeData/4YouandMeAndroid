package com.foryouandme.data.repository.consent.user.network.request

import com.squareup.moshi.Json

data class CreateUserConsentRequest(
    @Json(name = "agree") val agree: Boolean,
    @Json(name = "new_email") val email: String
)