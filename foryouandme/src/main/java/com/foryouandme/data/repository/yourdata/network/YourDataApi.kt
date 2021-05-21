package com.foryouandme.data.repository.yourdata.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.yourdata.network.response.UserDataAggregationResponse
import com.foryouandme.data.repository.yourdata.network.response.YourDataResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface YourDataApi {

    @GET("api/v1/studies/{study_id}/your_data")
    suspend fun getYourData(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): YourDataResponse

    @GET("/api/v1/studies/{study_id}/user_data_aggregations/{period}")
    suspend fun getUserDataAggregation(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String,
        @Path("period") period: String
    ): UserDataAggregationResponse

}