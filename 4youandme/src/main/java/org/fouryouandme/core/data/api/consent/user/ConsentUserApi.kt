package org.fouryouandme.core.data.api.consent.user

import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.consent.user.request.ConfirmUserConsentEmailRequest
import org.fouryouandme.core.data.api.consent.user.request.CreateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UpdateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UserConsentRequest
import org.fouryouandme.core.data.api.consent.user.response.ConsentUserResponse
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
    ): Unit

    @PATCH("api/v1/studies/{study_id}/user_consent")
    suspend fun updateUserConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<UpdateUserConsentRequest>
    ): Unit

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