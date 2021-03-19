package com.foryouandme.data.repository.auth.network.request

import com.squareup.moshi.Json

data class LoginRequest<T>(@Json(name = "user") val user: T)

data class PhoneLoginRequest(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "verification_code") val verificationCode: String
)

data class PinLoginRequest(
    @Json(name = "email") val pin: String,
)