package org.fouryouandme.core.data.api.wearable

import arrow.integrations.retrofit.adapter.CallK
import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.wearable.response.WearableResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface WearableApi {

    @GET("api/v1/studies/{study_id}/wearable")
    fun getWearable(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): CallK<ObjectDocument<WearableResponse>>

}