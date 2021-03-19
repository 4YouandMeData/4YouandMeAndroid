package com.foryouandme.data.repository.auth.network.request

import com.squareup.moshi.Json

data class LoginRequest(@Json(name = "user") val phone: PhoneLoginRequest)

data class PhoneLoginRequest(
    @Json(name = "phone_number") val phoneNumber: String,
    @Json(name = "verification_code") val verificationCode: String
)