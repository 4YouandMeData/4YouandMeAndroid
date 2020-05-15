package org.fouryouandme.core.data.api.auth

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.auth.request.LoginRequest
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import org.fouryouandme.core.data.api.auth.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("api/v1/studies/{study_id}/auth/verify_phone_number")
    fun verifyPhoneNumber(
        @Path("study_id") studyId: String,
        @Body request: PhoneNumberVerificationRequest
    ): CallK<Unit>

    @POST("api/v1/studies/{study_id}/auth/login")
    fun login(
        @Path("study_id") studyId: String,
        @Body request: LoginRequest
    ): CallK<UserResponse>
}