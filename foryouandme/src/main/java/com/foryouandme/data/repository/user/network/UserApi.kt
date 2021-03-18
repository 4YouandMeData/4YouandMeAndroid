package com.foryouandme.data.repository.user.network

import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest
import com.foryouandme.data.datasource.network.Headers
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserApi {

    @GET("api/v1/users/me")
    suspend fun getUser(@Header(Headers.AUTH) token: String): UserResponse

    @PATCH("api/v1/users/me")
    suspend fun updateUserTimeZone(
        @Header(Headers.AUTH) token: String,
        @Body request: UserTimeZoneUpdateRequest
    )

    @PATCH("api/v1/users/me")
    suspend fun updateUserCustomData(
        @Header(Headers.AUTH) token: String,
        @Body request: UserCustomDataUpdateRequest
    )

}