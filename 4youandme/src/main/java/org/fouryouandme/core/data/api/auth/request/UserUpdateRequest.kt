package org.fouryouandme.core.data.api.auth.request

import com.squareup.moshi.Json
import org.fouryouandme.core.data.api.auth.request.UserCustomDataItemRequest.Companion.asRequest
import org.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_DATE
import org.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_ITEMS
import org.fouryouandme.core.data.api.auth.response.USER_CUSTOM_DATA_TYPE_STRING
import org.fouryouandme.core.entity.user.UserCustomData
import org.fouryouandme.core.entity.user.UserCustomDataItem
import org.fouryouandme.core.entity.user.UserCustomDataType

data class UserUpdateRequest(
    @Json(name = "user") val user: UserUpdateDataRequest
) {

    companion object {

        fun List<UserCustomData>.asRequest(): UserUpdateRequest =
            UserUpdateRequest(
                user = UserUpdateDataRequest(
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

data class UserUpdateDataRequest(
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
