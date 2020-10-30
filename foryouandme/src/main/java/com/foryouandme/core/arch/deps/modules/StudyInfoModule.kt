package com.foryouandme.core.arch.deps.modules


import com.foryouandme.core.arch.deps.Environment
import com.foryouandme.core.data.api.studyinfo.StudyInfoApi

data class StudyInfoModule(
    val api: StudyInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)