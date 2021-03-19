package com.foryouandme.domain.usecase.analytics

import javax.inject.Inject

class SetAnalyticsUserIdUseCase @Inject constructor(
    private val repository: AnalyticsRepository
) {

    suspend operator fun invoke(id: String) {

        repository.setUserId(id)

    }

}