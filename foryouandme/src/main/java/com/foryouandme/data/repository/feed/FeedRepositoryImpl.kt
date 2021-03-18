package com.foryouandme.data.repository.feed

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.feed.network.FeedApi
import com.foryouandme.data.repository.feed.network.response.toFeedItems
import com.foryouandme.domain.usecase.feed.FeedRepository
import com.foryouandme.entity.feed.Feed
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.toPagedList
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val api: FeedApi,
    private val authErrorInterceptor: AuthErrorInterceptor,
    private val moshi: Moshi
) : FeedRepository {

    override suspend fun getFeed(
        token: String,
        page: Int,
        pageSize: Int
    ): PagedList<Feed> =
        authErrorInterceptor.execute { api.getFeeds(token,page, pageSize) }
            .toFeedItems(moshi)
            .let { it.toPagedList(page, it.size < page) }

}