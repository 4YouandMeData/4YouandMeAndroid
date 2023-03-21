package com.foryouandme.domain.usecase.study

import com.foryouandme.domain.policy.Policy
import com.foryouandme.entity.study.Study
import com.foryouandme.entity.study.StudyInfo

interface StudyRepository {
    suspend fun fetchStudy(): Study
    suspend fun fetchStudyInfo(token: String, studyId: String, policy: Policy): StudyInfo?

}