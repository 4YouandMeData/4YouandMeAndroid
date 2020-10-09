package org.fouryouandme.core.data.api.auth

import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.auth.request.LoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.data.api.auth.request.UserUpdateRequest
import org.fouryouandme.core.data.api.auth.response.UserResponse
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
    suspend fun updateUser(
        @Header(Headers.AUTH) token: String,
        @Body request: UserUpdateRequest
    ): Unit

}