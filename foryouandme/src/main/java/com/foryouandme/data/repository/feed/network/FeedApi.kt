package com.foryouandme.data.repository.feed.network

import com.foryouandme.data.repository.feed.network.response.FeedResponse
import com.foryouandme.data.datasource.network.Headers
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FeedApi {

    @GET("api/v1/feeds")
    suspend fun getFeeds(
        @Header(Headers.AUTH) token: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Array<FeedResponse>

}

