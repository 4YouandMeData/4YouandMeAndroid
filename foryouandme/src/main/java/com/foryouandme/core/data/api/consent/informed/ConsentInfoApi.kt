package com.foryouandme.core.data.api.consent.informed

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.core.data.api.consent.informed.response.ConsentInfoResponse
import moe.banana.jsonapi2.ObjectDocument
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