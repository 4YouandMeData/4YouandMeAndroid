package com.foryouandme.data.repository.phase.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.feed.network.response.FeedResponse
import com.foryouandme.data.repository.phase.network.request.CloseUserStudyPhaseRequest
import retrofit2.http.*

interface PhaseApi {

    @PATCH("api/v1/user_study_phases/{user_study_phase_id}")
    suspend fun closeStudyPhase(
        @Header(Headers.AUTH) token: String,
        @Query("user_study_phase_id") userStudyPhaseId: String,
        @Body request: CloseUserStudyPhaseRequest
    )

    @GET("api/v1/study_phases/{study_phase_id}/user_study_phases")
    suspend fun addStudyPhase(
        @Header(Headers.AUTH) token: String,
        @Path("study_phase_id") studyPhaseId: String
    ): FeedResponse

}

