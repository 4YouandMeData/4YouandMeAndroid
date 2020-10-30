package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.feed.FeedApi
import com.squareup.moshi.Moshi

data class FeedModule(
    val api: FeedApi,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)