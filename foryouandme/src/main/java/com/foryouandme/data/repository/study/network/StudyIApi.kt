package com.foryouandme.data.repository.study.network

import com.foryouandme.data.datasource.network.Headers
import com.foryouandme.data.repository.study.network.response.StudyInfoResponse
import com.foryouandme.data.repository.study.network.response.StudyResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StudyIApi {

    @GET("/api/v1/studies/{study_id}/study_info")
    suspend fun getStudyInfo(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<StudyInfoResponse>


    @GET("/api/v1/studies/{study_id}")
    suspend fun getStudy(
        @Path("study_id") studyId: String
    ): StudyResponse
}