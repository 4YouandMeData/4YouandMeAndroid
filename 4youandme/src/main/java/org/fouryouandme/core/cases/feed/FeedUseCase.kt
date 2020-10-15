package org.fouryouandme.core.cases.feed

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.FeedModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.feed.FeedRepository.fetchFeeds
import org.fouryouandme.core.entity.feed.Feed

object FeedUseCase {

    suspend fun FeedModule.getFeeds(): Either<FourYouAndMeError, List<Feed>> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchFeeds(it) }

}