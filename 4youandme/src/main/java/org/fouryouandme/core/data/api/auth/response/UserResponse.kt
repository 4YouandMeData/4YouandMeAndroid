package org.fouryouandme.core.data.api.auth.response

import arrow.core.Either
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.user.User

data class UserResponse(
    @Json(name = "data") val data: UserDataResponse? = null
) {

    suspend fun toUser(token: String): User? =
        Either.catch {

            User(
                id = data?.id!!,
                email = data.attributes?.email,
                phoneNumber = data.attributes?.phoneNumber,
                daysInStudy = data.attributes?.daysInStudy ?: 0,
                identities = data.attributes?.identities ?: emptyList(),
                onBoardingCompleted = data.attributes?.onBoardingCompleted!!,
                token = token
            )

        }.orNull()

}

data class UserDataResponse(
    @Json(name = "id") val id: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "attributes") val attributes: UserAttributesResponse? = null
)

data class UserAttributesResponse(
    @Json(name = "email") val email: String? = null,
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "days_in_study") val daysInStudy: Int? = null,
    @Json(name = "identities") val identities: List<String>? = null,
    @Json(name = "on_boarding_completed") val onBoardingCompleted: Boolean? = null,
)
