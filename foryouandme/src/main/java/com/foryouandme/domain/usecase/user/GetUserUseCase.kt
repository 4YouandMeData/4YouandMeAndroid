package com.foryouandme.domain.usecase.user

import android.util.Log
import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataType
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val settings: StudySettings,
    private val getTokenUseCase: GetTokenUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val updateUserCustomDataUseCase: UpdateUserCustomDataUseCase
) {

    suspend operator fun invoke(
        policy: Policy = Policy.Network,
        statusCheck: Boolean = true
    ): User {

        val configuration = getConfigurationUseCase(Policy.LocalFirst)

        val user = when (policy) {
            Policy.LocalFirst -> repository.loadUser() ?: invoke(Policy.Network, statusCheck)
            Policy.Network -> {

                val token = getTokenUseCase()
                val user = repository.getUser(token, configuration)!!

                // if user has empty custom data update it with default configuration
                val defaultUserCustomData =
                    defaultUserCustomData(configuration.text.phases, configuration.phases)
                if (settings.useCustomData && user.customData.size != defaultUserCustomData.size) {

                    val customData =
                        defaultUserCustomData.map { default ->
                            user.customData.find { it.identifier == default.identifier } ?: default
                        }

                    updateUserCustomDataUseCase(customData)
                    repository.getUser(token, configuration)!!
                } else user
            }
        }

        if (statusCheck) {
            // TODO: Remove hardcoded user status reset
            val deliveryDate =
                user.customData
                    .find { it.identifier == UserCustomData.DELIVERY_DATE_IDENTIFIER }
                    ?.value

            val userPhase = user.phase?.phase?.name
            val isInSecondPhase =
                userPhase != null && userPhase == configuration.text.phases.getOrNull(index = 1)

            Log.d("TEST_TEST", "$deliveryDate  $isInSecondPhase")

            if (deliveryDate != null && isInSecondPhase.not()) {
                val customData =
                    user.customData
                        .map {
                            if (it.identifier == UserCustomData.DELIVERY_DATE_IDENTIFIER)
                                it.copy(value = null)
                            else it
                        }
                updateUserCustomDataUseCase(customData)
            }
        }

        return user
    }

    // TODO: remove this and handle default configuration from server
    private fun defaultUserCustomData(
        phasesOrder: List<String>,
        phases: List<StudyPhase>
    ): List<UserCustomData> =
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
                phase =
                phasesOrder
                    .getOrNull(index = 1)
                    ?.let { phase -> phases.find { it.name == phase } }
            )
        )

}