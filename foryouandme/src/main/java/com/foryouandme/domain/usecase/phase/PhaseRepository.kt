package com.foryouandme.domain.usecase.phase

import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.phase.UserStudyPhase

interface PhaseRepository {

    suspend fun closeStudyPhases(userStudyPhase: UserStudyPhase)
    suspend fun addStudyPhase(studyPhase: StudyPhase)

}