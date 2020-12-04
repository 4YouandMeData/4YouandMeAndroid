package com.foryouandme.core.arch.deps.modules

import com.foryouandme.data.datasource.Environment
import com.foryouandme.core.data.api.survey.SurveyApi

data class SurveyModule(
    val api: SurveyApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)