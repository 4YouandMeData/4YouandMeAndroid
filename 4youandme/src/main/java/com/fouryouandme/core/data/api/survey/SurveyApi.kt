package com.fouryouandme.core.data.api.survey

import com.fouryouandme.core.data.api.Headers
import com.fouryouandme.core.data.api.survey.response.SurveyResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface SurveyApi {

    @GET("api/v1/surveys/{id}")
    suspend fun getSurvey(
        @Header(Headers.AUTH) token: String,
        @Path("id") id: String
    ): SurveyResponse

}