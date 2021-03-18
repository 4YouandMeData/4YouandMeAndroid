package com.foryouandme.data.repository.user.network

import com.foryouandme.core.ext.catchToNull
import com.foryouandme.core.ext.mapNotNull
import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataItem
import com.foryouandme.entity.user.UserCustomDataType
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource
import org.threeten.bp.ZoneId

@JsonApi(type = "user")
data class UserResponse(
    @field:Json(name = "email") val email: String? = null,
    @field:Json(name = "phone_number") val phoneNumber: String? = null,
    @field:Json(name = "days_in_study") val daysInStudy: Int? = null,
    @field:Json(name = "identities") val identities: List<String>? = null,
    @field:Json(name = "on_boarding_completed") val onBoardingCompleted: Boolean? = null,
    @field:Json(name = "custom_data") val customData: List<UserCustomDataResponse>? = null,
    @field:Json(name = "time_zone") val timeZone: String? = null,
    @field:Json(name = "points") val points: Int? = null
) : Resource() {

    fun toUser(token: String): User? =
        when (null) {
            id, onBoardingCompleted, points -> null
            else ->
                User(
                    id = id,
                    email = email,
                    phoneNumber = phoneNumber,
                    daysInStudy = daysInStudy ?: 0,
                    identities =
                    identities?.mapNotNull { IntegrationApp.fromIdentifier(it) } ?: emptyList(),
                    onBoardingCompleted = onBoardingCompleted,
                    token = token,
                    customData = customData?.mapNotNull { it.toUserCustomData() } ?: emptyList(),
                    timeZone = catchToNull { ZoneId.of(timeZone) },
                    points = points
                )

        }

}

data class UserCustomDataResponse(
    @field:Json(name = "identifier") val identifier: String? = null,
    @field:Json(name = "value") val value: String? = null,
    @field:Json(name = "name") val name: String? = null,
    @field:Json(name = "type") val type: String? = null,
    @field:Json(name = "items") val items: List<UserCustomDataItemResponse>? = null,
) {

    fun toUserCustomData(): UserCustomData? {

        val type =
            when (type) {
                USER_CUSTOM_DATA_TYPE_STRING ->
                    UserCustomDataType.String
                USER_CUSTOM_DATA_TYPE_DATE ->
                    UserCustomDataType.Date
                USER_CUSTOM_DATA_TYPE_ITEMS -> {

                    val items =
                        items?.mapNotNull {
                            mapNotNull(it.identifier, it.value)
                                ?.let { (id, itemValue) ->
                                    UserCustomDataItem(id, itemValue)
                                }
                        } ?: emptyList()

                    if (items.isEmpty()) null
                    else UserCustomDataType.Items(items)
                }
                else -> null
            }

        return when (null) {
            identifier, name, type -> null
            else -> UserCustomData(identifier, value, name, type)
        }

    }

}

data class UserCustomDataItemResponse(
    @field:Json(name = "identifier") val identifier: String? = null,
    @field:Json(name = "value") val value: String? = null,
)

const val USER_CUSTOM_DATA_TYPE_STRING = "string"
const val USER_CUSTOM_DATA_TYPE_DATE = "date"
const val USER_CUSTOM_DATA_TYPE_ITEMS = "items"
