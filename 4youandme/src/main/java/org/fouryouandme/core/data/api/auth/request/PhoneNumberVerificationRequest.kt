package org.fouryouandme.core.data.api.auth.request

import com.squareup.moshi.Json

data class PhoneNumberVerificationRequest(@Json(name = "user") val phone: PhoneNumberRequest)

data class PhoneNumberRequest(@Json(name = "phone_number") val phoneNumber: String)