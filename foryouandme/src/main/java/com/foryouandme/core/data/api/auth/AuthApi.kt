package com.foryouandme.core.data.api.auth

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.core.data.api.auth.request.*
import com.foryouandme.core.data.api.auth.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("api/v1/studies/{study_id}/auth/verify_phone_number")
    suspend fun verifyPhoneNumber(
        @Path("study_id") studyId: String,
        @Body request: PhoneNumberVerificationRequest
    ): Response<Unit>

    @POST("api/v1/studies/{study_id}/auth/login")
    suspend fun login(
        @Path("study_id") studyId: String,
        @Body request: LoginRequest
    ): Response<UserResponse>

    @GET("api/v1/users/me")
    suspend fun getUser(@Header(Headers.AUTH) token: String): UserResponse

    @PATCH("api/v1/users/me")
    suspend fun updateUserCustomData(
        @Header(Headers.AUTH) token: String,
        @Body request: UserCustomDataUpdateRequest
    ): Unit

    @PATCH("api/v1/users/me")
    suspend fun updateUserTimeZone(
        @Header(Headers.AUTH) token: String,
        @Body request: UserTimeZoneUpdateRequest
    ): Unit

    @PATCH("api/v1/users/me/add_firebase_token")
    suspend fun updateFirebaseToken(
        @Header(Headers.AUTH) token: String,
        @Body request: UserFirebaseTokenUpdateRequest
    ): Unit

}