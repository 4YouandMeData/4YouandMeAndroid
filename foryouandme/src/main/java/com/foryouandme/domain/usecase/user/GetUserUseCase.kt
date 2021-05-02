package com.foryouandme.domain.usecase.user

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.policy.Policy
import com.foryouandme.entity.user.PREGNANCY_END_DATE_IDENTIFIER
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataType
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository,
    private val environment: Environment,
    private val getTokenUseCase: GetTokenUseCase,
    private val updateUserCustomDataUseCase: UpdateUserCustomDataUseCase
) {

    suspend operator fun invoke(policy: Policy = Policy.Network): User =
        when(policy) {
            Policy.LocalFirst -> repository.loadUser() ?: invoke(Policy.Network)
            Policy.Network -> {
                val token = getTokenUseCase()
                val user = repository.getUser(token)!!

                // if user has empty custom data update it with default configuration
                if (environment.useCustomData() && user.customData.isEmpty()) {

                    updateUserCustomDataUseCase(defaultUserCustomData())
                    repository.getUser(token)!!

                } else user
            }
        }

    // TODO: remove this and handle default configuration from server
    private fun defaultUserCustomData(): List<UserCustomData> =
        listOf(
            UserCustomData(
                identifier = PREGNANCY_END_DATE_IDENTIFIER,
                type = UserCustomDataType.Date,
                name = "Your due date",
                value = null
            )
        )

}