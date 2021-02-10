package com.foryouandme.domain.usecase.location

import com.foryouandme.entity.location.Location
import javax.inject.Inject

class GetHomeLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {

    suspend operator fun invoke(): Location? = repository.getHomeLocation()

}