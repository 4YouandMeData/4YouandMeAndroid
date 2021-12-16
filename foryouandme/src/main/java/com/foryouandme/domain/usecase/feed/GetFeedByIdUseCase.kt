package com.foryouandme.domain.usecase.feed

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.feed.Feed
import javax.inject.Inject

class GetFeedByIdUseCase @Inject constructor(
    private val repository: FeedRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(id: String): Feed? =
        repository.getById(getTokenUseCase(), id)

}