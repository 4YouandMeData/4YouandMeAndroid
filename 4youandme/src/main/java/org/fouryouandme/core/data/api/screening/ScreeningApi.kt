package org.fouryouandme.core.data.api.screening

import moe.banana.jsonapi2.ObjectDocument
import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.screening.response.ScreeningResponse
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