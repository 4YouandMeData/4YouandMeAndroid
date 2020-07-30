package org.fouryouandme.core.data.api.integration

import arrow.integrations.retrofit.adapter.CallK
import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.integration.response.IntegrationResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IntegrationApi {

    @GET("api/v1/studies/{study_id}/integration")
    fun getIntegration(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): CallK<ObjectDocument<IntegrationResponse>>

}