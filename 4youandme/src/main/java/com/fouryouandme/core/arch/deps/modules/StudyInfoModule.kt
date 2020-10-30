package com.fouryouandme.core.arch.deps.modules


import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.studyinfo.StudyInfoApi

data class StudyInfoModule(
    val api: StudyInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)