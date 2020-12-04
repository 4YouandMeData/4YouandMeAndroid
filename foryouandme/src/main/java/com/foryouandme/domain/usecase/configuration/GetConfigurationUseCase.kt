package com.foryouandme.domain.usecase.configuration

import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.policy.Policy.*
import com.foryouandme.entity.configuration.Configuration
import javax.inject.Inject

class GetConfigurationUseCase @Inject constructor(
    private val repository: ConfigurationRepository
) {

    suspend fun execute(policy: Policy): Configuration? =
        when (policy) {
            LocalOnly -> repository.loadConfiguration()
            Network -> repository.fetchConfiguration()
            LocalFirst -> execute(LocalOnly) ?: execute(Network)
        }


}