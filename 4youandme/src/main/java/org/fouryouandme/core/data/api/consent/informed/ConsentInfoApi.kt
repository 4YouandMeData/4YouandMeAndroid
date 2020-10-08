package org.fouryouandme.core.data.api.consent.informed

import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConsentInfoApi {

    @GET("api/v1/studies/{study_id}/informed_consent")
    suspend fun getConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentInfoResponse>

}