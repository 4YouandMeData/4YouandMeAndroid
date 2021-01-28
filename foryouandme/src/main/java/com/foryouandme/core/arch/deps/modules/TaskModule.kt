package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.task.network.TaskApi
import com.squareup.moshi.Moshi

data class TaskModule(
    val api: TaskApi,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)