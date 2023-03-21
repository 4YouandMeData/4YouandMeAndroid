package com.foryouandme.data.repository.study

import com.foryouandme.data.datasource.StudySettings
import com.foryouandme.data.datasource.network.AuthErrorInterceptor
import com.foryouandme.data.repository.study.network.StudyIApi
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.study.StudyRepository
import com.foryouandme.entity.study.Study
import com.foryouandme.entity.study.StudyInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyRepositoryImpl @Inject constructor(
    private val api: StudyIApi,
    private val settings: StudySettings,
    private val authErrorInterceptor: AuthErrorInterceptor
) : StudyRepository {

    private var studyInfo: StudyInfo? = null

    override suspend fun fetchStudy(): Study =
        api.getStudy(settings.studyId).toStudy()

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