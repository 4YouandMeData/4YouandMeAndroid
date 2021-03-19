package com.foryouandme.domain.usecase.auth

import com.foryouandme.data.datasource.Environment
import com.foryouandme.entity.user.PREGNANCY_END_DATE_IDENTIFIER
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataType
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val updateUserCustomDataUseCase: UpdateUserCustomDataUseCase,
    private val environment: Environment
) {

    suspend operator fun invoke(): User? {

        val user = repository.getUser(getTokenUseCase())

        return if(
            environment.useCustomData() &&
            user != null &&
            user.customData.isEmpty()
        ) {

            updateUserCustomDataUseCase(defaultUserCustomData())
            invoke()

        }
        else user

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