package com.foryouandme.data.repository.auth.integration.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.integration.network.response.IntegrationResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IntegrationApi {

    @GET("api/v1/studies/{study_id}/integration")
    suspend fun getIntegration(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<IntegrationResponse>

}