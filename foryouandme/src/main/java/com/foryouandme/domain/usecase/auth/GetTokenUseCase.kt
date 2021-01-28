package com.foryouandme.domain.usecase.auth

import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(): String = repository.getToken()

}