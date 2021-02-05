package com.foryouandme.domain.usecase.study

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import com.foryouandme.entity.studyinfo.StudyInfo
import javax.inject.Inject

class GetStudyInfoUseCase @Inject constructor(
    private val repository: StudyInfoRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): StudyInfo? =
        repository.fetchStudyInfo(getTokenUseCase(), environment.studyId())

}