package org.fouryouandme.core.data.api.consent.review

import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.consent.review.response.ConsentReviewResponse
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