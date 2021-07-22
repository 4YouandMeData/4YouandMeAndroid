package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class CreateConsentUserUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(email: String) {
        repository.createUserConsent(getTokenUseCase(), settings.studyId, email)
    }

}