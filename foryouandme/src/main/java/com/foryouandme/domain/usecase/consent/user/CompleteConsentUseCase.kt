
package com.foryouandme.domain.usecase.consent.user

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class CompleteConsentUseCase @Inject constructor(
    private val repository: ConsentUserRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke() {
        repository.completeConsent(getTokenUseCase(), settings.studyId)
    }

}