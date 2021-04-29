package com.foryouandme.domain.usecase.auth.consent

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.entity.consent.user.ConsentUser
import javax.inject.Inject

class GetConsentUserUseCase @Inject constructor(
    private val repository: ConsentRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): ConsentUser? =
        repository.getConsentUser(getTokenUseCase(), environment.studyId())

}