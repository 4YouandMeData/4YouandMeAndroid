package com.foryouandme.domain.usecase.user

import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke() {
        repository.clearUser()
    }

}