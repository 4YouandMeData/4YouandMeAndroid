package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class UpdateConsentUserUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(
        firstName: String,
        lastName: String,
        signatureBase64: String
    ) {
        repository.updateUserConsent(
            getTokenUseCase(),
            environment.studyId(),
            firstName,
            lastName,
            signatureBase64
        )
    }

}