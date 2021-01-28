package com.foryouandme.domain.usecase.analytics

import javax.inject.Inject

class SendAnalyticsEventUseCase @Inject constructor(
    private val repository: AnalyticsRepository
) {

    suspend operator fun invoke(event: AnalyticsEvent, provider: EAnalyticsProvider) {

        repository.sendEvent(event, provider)

    }

}