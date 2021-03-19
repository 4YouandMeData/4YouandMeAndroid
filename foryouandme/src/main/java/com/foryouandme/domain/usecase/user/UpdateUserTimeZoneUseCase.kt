package com.foryouandme.domain.usecase.user

import org.threeten.bp.ZoneId
import javax.inject.Inject

class UpdateUserTimeZoneUseCase @Inject constructor(
    private val repository: UserRepository,
    private val getTokenUseCase: GetTokenUseCase
) {

    suspend operator fun invoke(zoneId: ZoneId) {
        repository.updateUserTimeZone(getTokenUseCase(), zoneId)
    }

    suspend operator fun invoke(token: String, zoneId: ZoneId) {
        repository.updateUserTimeZone(token, zoneId)
    }

}