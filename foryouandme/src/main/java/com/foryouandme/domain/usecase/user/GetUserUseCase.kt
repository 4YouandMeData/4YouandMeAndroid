package com.foryouandme.domain.usecase.user

import com.foryouandme.core.ext.catchSuspend
import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.entity.user.*
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val settings: StudySettings,
    private val getTokenUseCase: GetTokenUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val updateUserCustomDataUseCase: UpdateUserCustomDataUseCase
) {

    suspend operator fun invoke(policy: Policy = Policy.Network): User =
        when (policy) {
            Policy.LocalFirst -> repository.loadUser() ?: invoke(Policy.Network)
            Policy.Network -> {

                val token = getTokenUseCase()
                val user = repository.getUser(token)!!
                val configuration = getConfigurationUseCase(Policy.LocalFirst)

                // if user has empty custom data update it with default configuration
                val defaultUserCustomData = defaultUserCustomData(configuration.text.phases)
                if (settings.useCustomData && user.customData.size != defaultUserCustomData.size) {

                    val customData =
                        defaultUserCustomData.map { default ->
                            user.customData.find { it.identifier == default.identifier } ?: default
                        }

                    updateUserCustomDataUseCase(customData)
                    repository.getUser(token)!!
                } else user
            }
        }

    // TODO: remove this and handle default configuration from server
    private fun defaultUserCustomData(phases: List<String>): List<UserCustomData> =
        listOf(
            UserCustomData(
                identifier = UserCustomData.PREGNANCY_END_DATE_IDENTIFIER,
                type = UserCustomDataType.Date,
                name = "Your due date",
                value = null,
                phase = null
            ),
            UserCustomData(
                identifier = UserCustomData.DELIVERY_DATE_IDENTIFIER,
                type = UserCustomDataType.Date,
                name = "Your delivery date",
                value = null,
                phase = phases.getOrNull(index = 1)
            )
        )

}