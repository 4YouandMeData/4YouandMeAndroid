package org.fouryouandme.core.data.api.auth

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.auth.request.PhoneNumberVerificationRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

    @POST("api/v1/studies/{study_id}/auth/verify_phone_number")
    fun verifyPhoneNumber(
        @Path("study_id") studyId: String,
        @Body request: PhoneNumberVerificationRequest
    ): CallK<Unit>
}