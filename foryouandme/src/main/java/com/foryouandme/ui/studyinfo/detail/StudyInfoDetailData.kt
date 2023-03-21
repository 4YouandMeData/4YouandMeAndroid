package com.foryouandme.ui.studyinfo.detail

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.study.StudyInfo


data class StudyInfoDetailState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val studyInfo: LazyData<StudyInfo> = LazyData.Empty,
    val type: EStudyInfoType = EStudyInfoType.INFO
) {

    companion object {

        fun mock(): StudyInfoDetailState =
            StudyInfoDetailState(
                configuration = Configuration.mock().toData(),
                studyInfo = StudyInfo.mock().toData()
            )

    }

}

sealed class StudyInfoDetailAction {

    object GetConfiguration: StudyInfoDetailAction()
    object GetStudyInfo: StudyInfoDetailAction()

}

enum class EStudyInfoType {

    INFO,
    REWARD,
    FAQ

}