package com.fouryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.core.flatMap
import com.fouryouandme.core.arch.deps.modules.StudyInfoModule
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.auth.AuthUseCase.getToken
import com.fouryouandme.core.cases.studyinfo.StudyInfoRepository.fetchStudyInfo
import com.fouryouandme.core.entity.studyinfo.StudyInfo

object StudyInfoUseCase {

    suspend fun StudyInfoModule.getStudyInfo(): Either<FourYouAndMeError, StudyInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchStudyInfo(it) }

}