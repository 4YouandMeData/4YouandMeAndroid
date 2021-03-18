package com.foryouandme.domain.usecase.auth

import javax.inject.Inject

class IsLoggedUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(): Boolean = repository.getTokenOrNull() != null

}