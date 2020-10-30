package com.foryouandme.core.cases.feed

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.FeedModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.feed.FeedRepository.fetchFeeds
import com.foryouandme.core.entity.feed.Feed

object FeedUseCase {

    suspend fun FeedModule.getFeeds(): Either<ForYouAndMeError, List<Feed>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchFeeds(it) }

}