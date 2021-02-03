package com.foryouandme.data.repository.survey.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.survey.network.response.SurveyResponse
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