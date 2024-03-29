package com.foryouandme.data.repository.auth.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.network.request.*
import com.foryouandme.data.repository.user.network.response.UserResponse
import com.foryouandme.data.repository.user.network.request.UserCustomDataUpdateRequest
import com.foryouandme.data.repository.user.network.request.UserFirebaseTokenUpdateRequest
import com.foryouandme.data.repository.user.network.request.UserTimeZoneUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("api/v1/studies/{study_id}/auth/verify_phone_number")
    suspend fun verifyPhoneNumber(
        @Path("study_id") studyId: String,
        @Body request: PhoneNumberVerificationRequest
    ): Response<Unit>

    @POST("api/v1/studies/{study_id}/auth/login")
    suspend fun phoneLogin(
        @Path("study_id") studyId: String,
        @Body request: LoginRequest<PhoneLoginRequest>
    ): Response<UserResponse>

    @POST("api/v1/studies/{study_id}/auth/email_login")
    suspend fun pinLogin(
        @Path("study_id") studyId: String,
        @Body request: LoginRequest<PinLoginRequest>
    ): Response<UserResponse>

    @GET("api/v1/users/me")
    suspend fun getUser(@Header(Headers.AUTH) token: String): UserResponse

    @PATCH("api/v1/users/me")
    suspend fun updateUserCustomData(
        @Header(Headers.AUTH) token: String,
        @Body request: UserCustomDataUpdateRequest
    )

    @PATCH("api/v1/users/me")
    suspend fun updateUserTimeZone(
        @Header(Headers.AUTH) token: String,
        @Body request: UserTimeZoneUpdateRequest
    )

    @PATCH("api/v1/users/me/add_firebase_token")
    suspend fun updateFirebaseToken(
        @Header(Headers.AUTH) token: String,
        @Body request: UserFirebaseTokenUpdateRequest
    )

}