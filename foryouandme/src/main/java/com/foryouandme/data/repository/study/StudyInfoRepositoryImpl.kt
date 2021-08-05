package com.foryouandme.data.repository.study

import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.study.network.StudyInfoApi
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.study.StudyInfoRepository
import com.foryouandme.entity.studyinfo.StudyInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyInfoRepositoryImpl @Inject constructor(
    private val api: StudyInfoApi,
    private val authErrorInterceptor: AuthErrorInterceptor
) : StudyInfoRepository {

    private var studyInfo: StudyInfo? = null

    override suspend fun fetchStudyInfo(
        token: String,
        studyId: String,
        policy: Policy
    ): StudyInfo? =
        when (policy) {
            Policy.LocalFirst ->
                if (studyInfo != null) studyInfo
                else fetchStudyInfo(token, studyId, Policy.Network)
            Policy.Network -> {
                val document = authErrorInterceptor.execute { api.getStudyInfo(token, studyId) }
                val data = document.get().toStudyInfo(document)
                studyInfo = data
                data
            }
        }

}