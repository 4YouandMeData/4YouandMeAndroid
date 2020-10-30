package com.fouryouandme.core.arch.deps.modules

import com.fouryouandme.core.arch.deps.Environment
import com.fouryouandme.core.data.api.survey.SurveyApi

data class SurveyModule(
    val api: SurveyApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)