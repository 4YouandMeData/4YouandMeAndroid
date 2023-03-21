package com.foryouandme.data.repository.phase

import com.foryouandme.domain.usecase.phase.PhaseRepository
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.phase.UserStudyPhase
import javax.inject.Inject

class PhaseRepositoryImpl @Inject constructor() : PhaseRepository {


    override suspend fun closeStudyPhases(userStudyPhase: UserStudyPhase) {

    }

    override suspend fun addStudyPhase(studyPhase: StudyPhase) {

    }

}