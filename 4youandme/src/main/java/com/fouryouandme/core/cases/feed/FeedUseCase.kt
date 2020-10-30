package com.fouryouandme.core.cases.feed

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.FeedModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.feed.FeedRepository.fetchFeeds
import com.fouryouandme.core.entity.feed.Feed

object FeedUseCase {

    suspend fun FeedModule.getFeeds(): Either<FourYouAndMeError, List<Feed>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchFeeds(it) }

}