package com.foryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.syntax.function.pipe
import com.foryouandme.core.arch.deps.modules.StudyInfoModule
import com.foryouandme.core.arch.deps.modules.unwrapToEither
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.entity.studyinfo.StudyInfo

object StudyInfoRepository {

    internal suspend fun StudyInfoModule.fetchStudyInfo(token: String): Either<ForYouAndMeError, StudyInfo?> =
        suspend { api.getStudyInfo(token, environment.studyId()) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toStudyInfo(it) }

}