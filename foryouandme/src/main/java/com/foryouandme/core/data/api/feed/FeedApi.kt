package com.foryouandme.core.data.api.feed

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.core.data.api.feed.response.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FeedApi {

    @GET("api/v1/feeds")
    suspend fun getFeeds(
        @Header(Headers.AUTH) token: String,
        @Query("q[active]") active: Boolean,
        @Query("q[not_completed]") notCompleted: Boolean,
        @Query("q[s]") order: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): Array<FeedResponse>

}

