package com.foryouandme.core.cases.feed

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.FeedModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.feed.FeedRepository.fetchFeeds
import com.foryouandme.entity.feed.Feed
import com.foryouandme.data.datasource.network.Order
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import com.giacomoparisi.recyclerdroid.core.paging.toPagedList

object FeedUseCase {

    suspend fun FeedModule.getFeeds(
        order: Order,
        page: Int,
        pageSize: Int
    ): Either<ForYouAndMeError, PagedList<Feed>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchFeeds(it, order, page, pageSize) }
            .map { it.toPagedList(page, it.size < page) }

}