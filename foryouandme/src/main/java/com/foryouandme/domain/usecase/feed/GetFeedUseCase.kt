package com.foryouandme.domain.usecase.feed

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.feed.Feed
import com.giacomoparisi.recyclerdroid.core.paging.PagedList
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val repository: FeedRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(page: Int, pageSize: Int): PagedList<Feed> =
        repository.getFeed(getTokenUseCase(), page, pageSize)

}