
package com.foryouandme.domain.usecase.consent.user

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.auth.GetTokenUseCase
import javax.inject.Inject

class CompleteConsentUseCase @Inject constructor(
    private val repository: ConsentUserRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke() {
        repository.completeConsent(getTokenUseCase(), environment.studyId())
    }

}