package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class UpdateConsentUserUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val settings: StudySettings
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        signatureBase64: String
    ) {
        repository.updateUserConsent(
            getTokenUseCase(),
            settings.studyId,
            firstName,
            lastName,
            signatureBase64
        )
    }

}