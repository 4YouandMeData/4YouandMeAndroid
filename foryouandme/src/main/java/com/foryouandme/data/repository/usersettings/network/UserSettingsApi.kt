package com.foryouandme.data.repository.usersettings.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.usersettings.network.response.UserSettingsResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface UserSettingsApi {

    @GET("/api/v1/user_setting")
    suspend fun getUserSettings(@Header(Headers.AUTH) token: String): UserSettingsResponse

//    @PATCH("/api/v1/user_setting")
//    suspend fun updateUserSettings(
//        @Header(Headers.AUTH) token: String,
//        @Body request: UserSettingsUpdateRequest
//    )

}