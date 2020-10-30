package com.fouryouandme.core.data.api.feed

import com.fouryouandme.core.data.api.Headers
import com.fouryouandme.core.data.api.feed.response.FeedResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface FeedApi {

    @GET("api/v1/feeds")
    suspend fun getFeeds(
        @Header(Headers.AUTH) token: String
    ): Array<FeedResponse>

}

