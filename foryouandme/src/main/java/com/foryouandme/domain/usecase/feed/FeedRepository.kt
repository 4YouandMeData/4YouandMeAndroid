package com.foryouandme.domain.usecase.feed

import com.foryouandme.entity.feed.Feed
import com.giacomoparisi.recyclerdroid.core.paging.PagedList

interface FeedRepository {

    suspend fun getFeed(
        token: String,
        page: Int,
        pageSize: Int
    ): PagedList<Feed>

}