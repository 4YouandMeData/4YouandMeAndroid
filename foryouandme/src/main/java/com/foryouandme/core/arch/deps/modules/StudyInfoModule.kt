package com.foryouandme.core.arch.deps.modules


import com.foryouandme.data.datasource.Environment
import com.foryouandme.data.repository.study.network.StudyInfoApi

data class StudyInfoModule(
    val api: StudyInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)