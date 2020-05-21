package org.fouryouandme.core.data.api.auth.response

import arrow.core.Option
import arrow.core.extensions.fx
import arrow.core.toOption
import com.squareup.moshi.Json
import org.fouryouandme.core.entity.user.User

data class UserResponse(
    @Json(name = "phone_number") val phoneNumber: String? = null,
    @Json(name = "id") val id: Int? = null,
    @Json(name = "email") val email: String? = null
) {

    fun toUser(token: String): Option<User> =
        Option.fx {
            User(id.toOption().bind(), email.toOption().bind(), token)
        }

}
