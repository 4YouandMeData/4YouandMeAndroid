package org.fouryouandme.core.data.api.consent.user

import arrow.integrations.retrofit.adapter.CallK
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.consent.user.request.ConfirmUserConsentEmailRequest
import org.fouryouandme.core.data.api.consent.user.request.CreateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UpdateUserConsentRequest
import org.fouryouandme.core.data.api.consent.user.request.UserConsentRequest
import retrofit2.http.*

interface ConsentUserApi {

    @POST("api/v1/studies/{study_id}/user_consent")
    fun createUserConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<CreateUserConsentRequest>
    ): CallK<Unit>

    @PATCH("api/v1/studies/{study_id}/user_consent")
    fun updateUserConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<UpdateUserConsentRequest>
    ): CallK<Unit>

    @PATCH("api/v1/studies/{study_id}/user_consent/confirm_email")
    fun confirmEmail(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Body request: UserConsentRequest<ConfirmUserConsentEmailRequest>
    ): CallK<Unit>

    @PATCH("api/v1/studies/{study_id}/user_consent/resend_confirmation_email")
    fun resendConfirmationEmail(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): CallK<Unit>

}