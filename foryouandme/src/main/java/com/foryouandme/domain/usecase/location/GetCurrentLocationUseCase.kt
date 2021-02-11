package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.LocationCoordinates
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(): LocationCoordinates? =
        repository.getCurrentLocation(TimeUnit.SECONDS.toMillis(60))

}