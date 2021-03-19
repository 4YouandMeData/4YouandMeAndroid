package com.foryouandme.domain.usecase.user

import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(): String = repository.getToken()

    suspend fun safe(): String? = repository.getTokenOrNull()

}