package com.foryouandme.data.repository.auth.consent.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.auth.consent.network.response.ConsentReviewResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConsentApi {

    @GET("api/v1/studies/{study_id}/consent")
    suspend fun getConsentReview(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentReviewResponse>

}