package com.foryouandme.domain.usecase.phase

import com.foryouandme.core.ext.catchSuspend
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.user.GetTokenUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.entity.phase.StudyPhase
import javax.inject.Inject

class SwitchStudyPhaseUseCase @Inject constructor(
    private val repository: PhaseRepository,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserUseCase: GetUserUseCase,
) {

    suspend operator fun invoke(studyPhase: StudyPhase) {
        catchSuspend(
            {
                // Get fresh user data from network
                val user = getUserUseCase(Policy.Network)
                val currentUserPhase = user.phase

                // Avoid to switch to the same phase
                if (currentUserPhase?.phase?.id != studyPhase.id) {
                    if (currentUserPhase != null)
                    // Close the previous user phase
                        repository.closeStudyPhase(getTokenUseCase(), currentUserPhase)

                    repository.addStudyPhase(getTokenUseCase(), studyPhase)
                }
            },
            {
                // Fix User Custom Data
                getUserUseCase(Policy.Network)
            }
        )
    }

}