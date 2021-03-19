package com.foryouandme.domain.usecase.configuration

import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.policy.Policy.LocalFirst
import com.foryouandme.domain.policy.Policy.Network
import com.foryouandme.entity.configuration.Configuration
import javax.inject.Inject

class GetConfigurationUseCase @Inject constructor(
    private val repository: ConfigurationRepository
) {

    suspend operator fun invoke(policy: Policy): Configuration =
        when (policy) {
            Network -> {
                repository.fetchConfiguration()
                    .also { repository.saveConfiguration(it) }
            }
            LocalFirst -> repository.loadConfiguration() ?: invoke(Network)
        }

}