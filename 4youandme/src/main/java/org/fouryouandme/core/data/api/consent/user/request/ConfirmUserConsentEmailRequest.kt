package org.fouryouandme.core.data.api.consent.user.request

import com.squareup.moshi.Json

data class ConfirmUserConsentEmailRequest(
    @Json(name = "email_confirmation_token") val code: String
)