package com.foryouandme.data.repository.consent.user.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.consent.user.network.request.*
import com.foryouandme.data.repository.consent.user.network.response.ConsentUserResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.Response
import retrofit2.http.*

interface ConsentUserApi {

    @GET("api/v1/studies/{study_id}/signature")
    suspend fun getConsentUser(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentUserResponse>

    @POST("api/v1/studies/{study_id}/user_consent")
    suspend fun createUserConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<CreateUserConsentRequest>
    )

    @PATCH("api/v1/studies/{study_id}/user_consent")
    suspend fun updateUserConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<UpdateUserConsentRequest>
    )

    @POST("api/v1/studies/{study_id}/user_consent")
    suspend fun completeConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<CompleteUserConsentRequest>
    )

    @PATCH("api/v1/studies/{study_id}/user_consent/confirm_email")
    suspend fun confirmEmail(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<ConfirmUserConsentEmailRequest>
    ): Response<Unit>

    @PATCH("api/v1/studies/{study_id}/user_consent/resend_confirmation_email")
    suspend fun resendConfirmationEmail(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): Response<Unit>

}