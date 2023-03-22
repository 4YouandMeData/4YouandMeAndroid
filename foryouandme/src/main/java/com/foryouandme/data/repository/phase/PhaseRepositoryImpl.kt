package com.foryouandme.data.repository.phase

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.phase.network.PhaseApi
import com.foryouandme.data.repository.phase.network.request.AddUserStudyPhaseRequest
import com.foryouandme.data.repository.phase.network.request.CloseUserStudyPhaseRequest
import com.foryouandme.data.repository.phase.network.request.UserStudyPhaseAddCustomDataRequest
import com.foryouandme.domain.usecase.phase.PhaseRepository
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.phase.UserStudyPhase
import javax.inject.Inject

class PhaseRepositoryImpl @Inject constructor(
    private val api: PhaseApi,
    private val authErrorInterceptor: AuthErrorInterceptor,
) : PhaseRepository {

    override suspend fun closeStudyPhase(token: String, userStudyPhase: UserStudyPhase) {
        authErrorInterceptor.execute {
            api.closeStudyPhase(token, userStudyPhase.id, CloseUserStudyPhaseRequest.build())
        }
    }

    override suspend fun addStudyPhase(token: String, studyPhase: StudyPhase) {
        authErrorInterceptor.execute {
            api.addStudyPhase(token, studyPhase.id, AddUserStudyPhaseRequest.build())
        }
    }

}