package com.foryouandme.domain.usecase.phase

import com.foryouandme.entity.phase.UserStudyPhase
import javax.inject.Inject

class CloseStudyPhaseUseCase @Inject constructor(
    private val repository: PhaseRepository
) {

    suspend operator fun invoke(userStudyPhase: UserStudyPhase) {
        repository.closeStudyPhases(userStudyPhase)
    }

}