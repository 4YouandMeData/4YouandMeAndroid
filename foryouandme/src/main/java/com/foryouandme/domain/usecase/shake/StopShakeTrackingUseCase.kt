package com.foryouandme.domain.usecase.shake

import javax.inject.Inject

class StopShakeTrackingUseCase @Inject constructor(
    private val repository: ShakeRepository
) {

    suspend operator fun invoke() {
        repository.stopShakeTracking()
    }

}