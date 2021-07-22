package com.foryouandme.domain.usecase.auth.integration

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.integration.Integration
import javax.inject.Inject

class GetIntegrationUseCase @Inject constructor(
    private val repository: IntegrationRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(): Integration? =
        repository.getIntegration(getTokenUseCase(), settings.studyId)

}