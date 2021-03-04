package com.foryouandme.data.repository.consent.user.network.request

import com.squareup.moshi.Json

data class ConfirmUserConsentEmailRequest(
    @Json(name = "email_confirmation_token") val code: String
)