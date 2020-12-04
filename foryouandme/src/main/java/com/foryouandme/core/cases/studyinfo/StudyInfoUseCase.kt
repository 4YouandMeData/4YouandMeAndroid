package com.foryouandme.core.cases.studyinfo

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.deps.modules.StudyInfoModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getToken
import com.foryouandme.core.cases.studyinfo.StudyInfoRepository.fetchStudyInfo
import com.foryouandme.entity.studyinfo.StudyInfo

object StudyInfoUseCase {

    suspend fun StudyInfoModule.getStudyInfo(): Either<ForYouAndMeError, StudyInfo?> =
        authModule.getToken(CachePolicy.MemoryFirst)
            .flatMap { fetchStudyInfo(it) }

}