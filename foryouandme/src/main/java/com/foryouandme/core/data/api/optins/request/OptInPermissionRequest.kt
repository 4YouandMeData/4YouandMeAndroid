package com.foryouandme.core.data.api.optins.request

import com.squareup.moshi.Json

data class OptInPermissionRequest
    (
    @Json(name = "user_permission") val userPermission: OptInUserPermissionRequest
)

data class OptInUserPermissionRequest(
    @Json(name = "agree") val agree: Boolean,
    @Json(name = "batch_code") val batchCode: String
)