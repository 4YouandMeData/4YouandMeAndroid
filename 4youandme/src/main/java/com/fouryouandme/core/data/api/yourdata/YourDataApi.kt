package com.fouryouandme.core.data.api.yourdata

import com.fouryouandme.core.data.api.Headers
import com.fouryouandme.core.data.api.yourdata.response.UserDataAggregationResponse
import com.fouryouandme.core.data.api.yourdata.response.YourDataResponse
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