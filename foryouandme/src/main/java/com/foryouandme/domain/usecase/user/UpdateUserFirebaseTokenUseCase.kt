package com.foryouandme.domain.usecase.user

import javax.inject.Inject

class UpdateUserFirebaseTokenUseCase @Inject constructor(
    private val repository: UserRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(firebaseToken: String) {
        repository.updateUserFirebaseToken(getTokenUseCase(), firebaseToken)
    }

    suspend operator fun invoke(token: String, firebaseToken: String) {
        repository.updateUserFirebaseToken(token, firebaseToken)
    }

}