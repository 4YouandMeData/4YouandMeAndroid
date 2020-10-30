package com.fouryouandme.core.data.api.studyinfo

import com.fouryouandme.core.data.api.Headers
import com.fouryouandme.core.data.api.studyinfo.response.StudyInfoResponse
import moe.banana.jsonapi2.ObjectDocument
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface StudyInfoApi {

    @GET("/api/v1/studies/{study_id}/study_info")
    suspend fun getStudyInfo(
        @Header(Headers.AUTH) token: String,
        @Path("study_id") studyId: String
    ): ObjectDocument<StudyInfoResponse>
}