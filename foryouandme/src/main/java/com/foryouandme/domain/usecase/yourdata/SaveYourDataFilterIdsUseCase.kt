package com.foryouandme.domain.usecase.yourdata

import com.foryouandme.entity.yourdata.YourDataFilter
import javax.inject.Inject

class SaveYourDataFilterIdsUseCase @Inject constructor(
    private val repository: YourDataRepository
) {

    suspend operator fun invoke(filters: List<YourDataFilter>) {
        repository.saveFilters(filters)
    }

}