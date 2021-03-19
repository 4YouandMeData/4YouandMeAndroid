package com.foryouandme.domain.usecase.user

import com.foryouandme.entity.user.UserCustomData
import javax.inject.Inject

class UpdateUserCustomDataUseCase @Inject constructor(
    private val repository: UserRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(data: List<UserCustomData>) {
        repository.updateUserCustomData(getTokenUseCase(), data)
    }

}