package com.fouryouandme.core.data.api.auth.request

import com.fouryouandme.core.data.api.auth.request.UserCustomDataItemRequest.Companion.asRequest
import com.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_DATE
import com.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_ITEMS
import com.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_STRING
import com.fouryouandme.core.entity.user.UserCustomData
import com.fouryouandme.core.entity.user.UserCustomDataItem
import com.fouryouandme.core.entity.user.UserCustomDataType
import com.squareup.moshi.Json

data class UserCustomDataUpdateRequest(
    @Json(name = "user") val user: UserCustomDataUpdateDataRequest
) {

    companion object {

        fun List<UserCustomData>.asRequest(): UserCustomDataUpdateRequest =
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

                        UserCustomDataRequest(
                            data.identifier,
                            data.value,
                            data.name,
                            type,
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
