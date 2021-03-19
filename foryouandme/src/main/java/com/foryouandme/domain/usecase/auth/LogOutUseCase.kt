package com.foryouandme.domain.usecase.auth

import com.foryouandme.domain.usecase.user.ClearUserUseCase
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val clearUserUseCase: ClearUserUseCase
) {

    suspend operator fun invoke() {
        clearUserUseCase()
    }

}