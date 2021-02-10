package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.Location
import javax.inject.Inject

class SaveHomeLocationUseCase @Inject constructor(
    val repository: LocationRepository
) {

    suspend operator fun invoke(location: Location) {
        repository.saveHomeLocation(location)
    }

}