package com.foryouandme.data.repository.consent.user.network.request

import com.squareup.moshi.Json

data class UpdateUserConsentRequest(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    @Json(name = "signature_base64") val signatureBase64: String
)