package org.fouryouandme.core.arch.deps.modules


import org.fouryouandme.core.arch.deps.Environment
import org.fouryouandme.core.data.api.studyinfo.StudyInfoApi

data class StudyInfoModule(
    val api: StudyInfoApi,
    val environment: Environment,
    val errorModule: ErrorModule,
    val authModule: AuthModule
)