package org.fouryouandme.core.data.api.optins.request

import com.squareup.moshi.Json

data class OptInPermissionRequest(@Json(name = "answer") val answer: OptInPermissionAnswerRequest)

data class OptInPermissionAnswerRequest(
    @Json(name = "agree") val agree: Boolean,
    @Json(name = "batch_code") val batchCode: String
)