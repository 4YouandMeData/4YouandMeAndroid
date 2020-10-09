package org.fouryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.syntax.function.pipe
import org.fouryouandme.core.arch.deps.modules.StudyInfoModule
import org.fouryouandme.core.arch.deps.modules.unwrapToEither
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.entity.studyinfo.StudyInfo

object StudyInfoRepository {

    internal suspend fun StudyInfoModule.fetchStudyInfo(token: String): Either<FourYouAndMeError, StudyInfo?> =
        suspend { api.getStudyInfo(token, environment.studyId()) }
            .pipe { errorModule.unwrapToEither(it) }
            .map { it.get().toStudyInfo(it) }

}