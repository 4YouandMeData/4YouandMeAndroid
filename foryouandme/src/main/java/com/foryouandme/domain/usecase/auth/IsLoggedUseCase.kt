package com.foryouandme.domain.usecase.auth

import com.foryouandme.domain.usecase.user.GetTokenUseCase
import javax.inject.Inject

class IsLoggedUseCase @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(): Boolean = getTokenUseCase.safe() != null

}