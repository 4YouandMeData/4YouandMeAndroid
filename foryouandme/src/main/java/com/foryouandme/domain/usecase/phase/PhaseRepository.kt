package com.foryouandme.domain.usecase.phase

import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.phase.UserStudyPhase

interface PhaseRepository {

    suspend fun closeStudyPhase(token: String, userStudyPhase: UserStudyPhase)
    suspend fun addStudyPhase(token: String, studyPhase: StudyPhase)

}