package com.foryouandme.data.repository.consent.user.network.request

import com.squareup.moshi.Json

data class UserConsentRequest<T>(@Json(name = "user_consent") val userConsent: T)