package com.foryouandme.domain.usecase.push

import javax.inject.Inject

class GetPushTokenUseCase @Inject constructor(
    private val repository: PushRepository
) {

    suspend operator fun invoke(): String = repository.getPushToken()

}