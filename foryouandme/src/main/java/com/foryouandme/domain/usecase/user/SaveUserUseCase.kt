package com.foryouandme.domain.usecase.user

import com.foryouandme.entity.user.User
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }

}