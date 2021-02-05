package com.foryouandme.domain.usecase.study

import com.foryouandme.entity.studyinfo.StudyInfo

interface StudyInfoRepository {

    suspend fun fetchStudyInfo(token: String, studyId: String): StudyInfo?

}