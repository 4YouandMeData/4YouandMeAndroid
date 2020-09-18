package org.fouryouandme.core.arch.deps.modules

import com.squareup.moshi.Moshi
import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.task.TaskApi

data class TaskModule(
    val api: TaskApi,
    val moshi: Moshi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)