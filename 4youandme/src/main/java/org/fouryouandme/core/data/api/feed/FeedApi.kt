package org.fouryouandme.core.data.api.feed

import org.fouryouandme.core.data.api.Headers
import org.fouryouandme.core.data.api.feed.response.FeedResponse
import retrofit2.http.*

interface FeedApi {

    @GET("api/v1/feeds")
    suspend fun getFeeds(
        @Header(Headers.AUTH) token: String
    ): Array<FeedResponse>

}

