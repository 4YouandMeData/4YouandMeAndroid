package com.foryouandme.domain.usecase.study

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.studyinfo.StudyInfo
import javax.inject.Inject

class GetStudyInfoUseCase @Inject constructor(
    private val repository: StudyInfoRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(policy: Policy): StudyInfo? =
        repository.fetchStudyInfo(getTokenUseCase(), settings.studyId, policy)

}