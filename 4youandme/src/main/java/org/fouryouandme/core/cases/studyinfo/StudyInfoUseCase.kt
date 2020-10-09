package org.fouryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.core.flatMap
import org.fouryouandme.core.arch.deps.modules.StudyInfoModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getToken
import org.fouryouandme.core.cases.studyinfo.StudyInfoRepository.fetchStudyInfo
import org.fouryouandme.core.entity.studyinfo.StudyInfo

object StudyInfoUseCase {

    suspend fun StudyInfoModule.getStudyInfo(): Either<FourYouAndMeError, StudyInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchStudyInfo(it) }

}