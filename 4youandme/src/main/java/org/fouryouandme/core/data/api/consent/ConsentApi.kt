package org.fouryouandme.core.data.api.consent

import arrow.integrations.retrofit.adapter.CallK
import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.consent.response.ConsentResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConsentApi {

    @GET("api/v1/studies/{study_id}/informed_consent")
    fun getConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): CallK<ObjectDocument<ConsentResponse>>

}