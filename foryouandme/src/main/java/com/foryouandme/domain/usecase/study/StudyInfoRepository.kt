package com.foryouandme.domain.usecase.study

import com.foryouandme.domain.policy.Policy
import com.foryouandme.entity.studyinfo.StudyInfo

interface StudyInfoRepository {

    suspend fun fetchStudyInfo(token: String, studyId: String, policy: Policy): StudyInfo?

}