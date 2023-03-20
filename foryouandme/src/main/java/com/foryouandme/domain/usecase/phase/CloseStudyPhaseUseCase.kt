package com.foryouandme.domain.usecase.phase

import javax.inject.Inject

class CloseStudyPhaseUseCase @Inject constructor(
    private val repository: PhaseRepository
) {

    suspend operator fun invoke() {
        repository.getStudyPhases()
    }

}