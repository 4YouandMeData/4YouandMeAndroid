package com.foryouandme.data.repository.feed.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.feed.network.response.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {

    @GET("api/v1/feeds")
    suspend fun getFeeds(
        @Header(Headers.AUTH) token: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Array<FeedResponse>

    @GET("api/v1/tasks/{id}")
    suspend fun getFeed(
        @Header(Headers.AUTH) token: String,
        @Path("id") id: String
    ): FeedResponse

}

