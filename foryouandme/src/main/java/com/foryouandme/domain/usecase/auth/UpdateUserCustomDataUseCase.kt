package com.foryouandme.domain.usecase.auth

import com.foryouandme.entity.user.UserCustomData
import javax.inject.Inject

class UpdateUserCustomDataUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(data: List<UserCustomData>) {
        authRepository.updateUserCustomData(getTokenUseCase(), data)
    }

}