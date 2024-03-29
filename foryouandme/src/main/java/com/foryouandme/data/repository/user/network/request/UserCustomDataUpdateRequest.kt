package com.foryouandme.data.repository.user.network.request

import com.foryouandme.data.repository.user.network.request.UserCustomDataItemRequest.Companion.asRequest
import com.foryouandme.data.repository.user.network.response.USER_CUSTOM_DATA_TYPE_DATE
import com.foryouandme.data.repository.user.network.response.USER_CUSTOM_DATA_TYPE_ITEMS
import com.foryouandme.data.repository.user.network.response.USER_CUSTOM_DATA_TYPE_STRING
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataItem
import com.foryouandme.entity.user.UserCustomDataType
import com.squareup.moshi.Json

data class UserCustomDataUpdateRequest(
    @Json(name = "user") val user: UserCustomDataUpdateDataRequest
) {

    companion object {

        fun List<UserCustomData>.asRequest(
            configuration: Configuration
        ): UserCustomDataUpdateRequest =
            UserCustomDataUpdateRequest(
                user = UserCustomDataUpdateDataRequest(
                    customData = map { data ->

                        val type =
                            when (data.type) {
                                UserCustomDataType.String -> USER_CUSTOM_DATA_TYPE_STRING
                                UserCustomDataType.Date -> USER_CUSTOM_DATA_TYPE_DATE
                                is UserCustomDataType.Items -> USER_CUSTOM_DATA_TYPE_ITEMS
                            }

                        val items =
                            when (data.type) {
                                is UserCustomDataType.Items ->
                                    data.type.items.map { it.asRequest() }
                                else ->
                                    emptyList()
                            }

                        val phaseIndex =
                            data.phase?.name
                                ?.let { configuration.text.phases.indexOf(it) }
                                ?.let { if (it < 0) null else it }

                        UserCustomDataRequest(
                            data.identifier,
                            data.value,
                            data.name,
                            type,
                            phaseIndex,
                            items
                        )

                    }
                )
            )

    }

}

data class UserCustomDataUpdateDataRequest(
    @Json(name = "custom_data") val customData: List<UserCustomDataRequest>
)

data class UserCustomDataRequest(
    @Json(name = "identifier") val identifier: String,
    @Json(name = "value") val value: String? = null,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "phase") val phase: Int?,
    @Json(name = "items") val items: List<UserCustomDataItemRequest>,
)

data class UserCustomDataItemRequest(
    @Json(name = "identifier") val identifier: String,
    @Json(name = "value") val value: String,
) {

    companion object {

        fun UserCustomDataItem.asRequest(): UserCustomDataItemRequest =
            UserCustomDataItemRequest(identifier, value)

    }

}
