package com.foryouandme.data.repository.auth.consent.network.request

import com.squareup.moshi.Json
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

data class OptInPermissionRequest(
    @Json(name = "user_permission") val userPermission: OptInUserPermissionRequest
) {

    companion object {

        fun build(agree: Boolean): OptInPermissionRequest =
            OptInPermissionRequest(
                OptInUserPermissionRequest(
                    agree,
                    ZonedDateTime.now(ZoneOffset.UTC)
                        .format(
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")
                        )
                )
            )

    }

}

data class OptInUserPermissionRequest(
    @Json(name = "agree") val agree: Boolean,
    @Json(name = "batch_code") val batchCode: String
)