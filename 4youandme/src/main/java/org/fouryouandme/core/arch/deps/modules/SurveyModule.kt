package org.fouryouandme.core.arch.deps.modules

import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.survey.SurveyApi

data class SurveyModule(
    val api: SurveyApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)