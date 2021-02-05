package com.foryouandme.data.repository.study

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.study.network.StudyInfoApi
import com.foryouandme.domain.usecase.study.StudyInfoRepository
import com.foryouandme.entity.studyinfo.StudyInfo
import javax.inject.Inject

class StudyInfoRepositoryImpl @Inject constructor(
    private val api: StudyInfoApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : StudyInfoRepository {

    override suspend fun fetchStudyInfo(token: String, studyId: String): StudyInfo? {

        val document = authErrorInterceptor.execute { api.getStudyInfo(token, studyId) }
        return document.get().toStudyInfo(document)

    }

}