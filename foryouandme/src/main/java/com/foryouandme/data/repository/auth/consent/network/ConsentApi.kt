package com.foryouandme.data.repository.auth.consent.network

import com.foryouandme.data.repository.consent.user.network.request.OptInPermissionRequest
import com.foryouandme.data.repository.auth.consent.network.response.OptInsResponse
import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.consent.network.response.ConsentInfoResponse
import com.foryouandme.data.repository.auth.consent.network.response.ConsentReviewResponse
import com.foryouandme.data.repository.consent.user.network.request.*
import com.foryouandme.data.repository.auth.consent.network.response.ConsentUserResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.Response
import retrofit2.http.*

interface ConsentApi {

    @GET("api/v1/studies/{study_id}/consent")
    suspend fun getConsentReview(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentReviewResponse>

    @GET("api/v1/studies/{study_id}/informed_consent")
    suspend fun getConsentInfo(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentInfoResponse>

    @GET("api/v1/studies/{study_id}/opt_in")
    suspend fun getOptIns(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<OptInsResponse>

    @POST("api/v1/permissions/{permission_id}/user_permission")
    suspend fun setPermission(
        @Header(Headers.AUTH) token: String,
        @Path("permission_id") permissionId: String,
        @Body request: OptInPermissionRequest
    )

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