package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.data.api.feed.FeedApi
import com.squareup.moshi.Moshi

data class FeedModule(
    val api: FeedApi,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)