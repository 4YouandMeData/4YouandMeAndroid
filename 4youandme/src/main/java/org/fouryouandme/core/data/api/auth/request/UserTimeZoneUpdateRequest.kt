package org.fouryouandme.core.data.api.auth.request

import com.squareup.moshi.Json
import org.threeten.bp.ZoneId

data class UserTimeZoneUpdateRequest(
    @Json(name = "user") val user: UserTimeZoneDataUpdateRequest
) {

    companion object {

        fun ZoneId.asRequest(): UserTimeZoneUpdateRequest =
            UserTimeZoneUpdateRequest(
                user = UserTimeZoneDataUpdateRequest(
                    timeZone = id
                )
            )

    }

}

data class UserTimeZoneDataUpdateRequest(
    @Json(name = "time_zone") val timeZone: String
)

