package com.fouryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.syntax.function.pipe
import com.fouryouandme.core.arch.deps.modules.StudyInfoModule
import com.fouryouandme.core.arch.deps.modules.unwrapToEither
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.entity.studyinfo.StudyInfo

object StudyInfoRepository {

    internal suspend fun StudyInfoModule.fetchStudyInfo(token: String): Either<FourYouAndMeError, StudyInfo?> =
        suspend { api.getStudyInfo(token, environment.studyId()) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toStudyInfo(it) }

}