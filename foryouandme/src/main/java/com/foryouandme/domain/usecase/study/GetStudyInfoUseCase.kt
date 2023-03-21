package com.foryouandme.domain.usecase.study

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.entity.study.StudyInfo
import javax.inject.Inject

class GetStudyInfoUseCase @Inject constructor(
    private val repository: StudyRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(policy: Policy): StudyInfo? {
        val studyInfo = repository.fetchStudyInfo(getTokenUseCase(), settings.studyId, policy)
        val user = getUserUseCase(Policy.LocalFirst)
        return if (user.phase != null) {
            val configuration = getConfigurationUseCase(Policy.LocalFirst)
            val phase = configuration.phases.find { it.name == user.phase.phase.name }
            studyInfo?.copy(faqPage = phase?.faq ?: studyInfo.faqPage)
        } else studyInfo
    }

}