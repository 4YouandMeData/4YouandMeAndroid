package com.foryouandme.data.repository.configuration.network

import com.foryouandme.data.repository.configuration.network.response.ConfigurationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ConfigurationApi {

    @GET("/api/v1/studies/{study_id}/configuration")
    suspend fun getConfiguration(@Path("study_id") studyId: String): ConfigurationResponse

}