package com.foryouandme.core.data.api.auth.request

import com.squareup.moshi.Json

data class UserFirebaseTokenUpdateRequest(
    @Json(name = "user") val user: UserFirebaseTokenDataUpdateRequest
)

data class UserFirebaseTokenDataUpdateRequest(
    @Json(name = "firebase_token") val firebaseToken: String
)

