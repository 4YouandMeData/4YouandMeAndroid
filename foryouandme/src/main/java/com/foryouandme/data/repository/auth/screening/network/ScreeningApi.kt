package com.foryouandme.data.repository.auth.screening.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.screening.network.response.ScreeningResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ScreeningApi {

    @GET("api/v1/studies/{study_id}/screening")
    suspend fun getScreening(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ScreeningResponse>

}