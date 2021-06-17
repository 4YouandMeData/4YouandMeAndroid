package com.foryouandme.domain.usecase.yourdata

import javax.inject.Inject

class GetYourDataFilterIdsUseCase @Inject constructor(
    private val repository: YourDataRepository
) {

    suspend operator fun invoke(): List<String> =
        repository.getFiltersIds()

}