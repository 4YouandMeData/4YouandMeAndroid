package com.foryouandme.domain.usecase.user

import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.entity.user.UserCustomData
import javax.inject.Inject

class UpdateUserCustomDataUseCase @Inject constructor(
    private val repository: UserRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val configurationUseCase: GetConfigurationUseCase
) {

    suspend operator fun invoke(data: List<UserCustomData>) {
        repository.updateUserCustomData(
            getTokenUseCase(),
            data,
            configurationUseCase(Policy.LocalFirst)
        )
    }

}