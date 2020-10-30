package com.foryouandme.core.data.api.consent.review

import com.foryouandme.core.data.api.Headers
import com.foryouandme.core.data.api.consent.review.response.ConsentReviewResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ConsentReviewApi {

    @GET("api/v1/studies/{study_id}/consent")
    suspend fun getConsent(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<ConsentReviewResponse>

}