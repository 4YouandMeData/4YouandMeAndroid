package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.task.TaskApi
import com.squareup.moshi.Moshi

data class TaskModule(
    val api: TaskApi,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)