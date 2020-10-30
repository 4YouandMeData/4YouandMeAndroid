package com.fouryouandme.core.data.api.consent.user.request

import com.squareup.moshi.Json

data class UserConsentRequest<T>(@Json(name = "user_consent") val userConsent: T)