package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class CreateConsentUserUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(email: String) {
        repository.createUserConsent(getTokenUseCase(), environment.studyId(), email)
    }

}