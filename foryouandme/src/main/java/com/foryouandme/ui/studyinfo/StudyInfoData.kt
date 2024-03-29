package com.foryouandme.ui.studyinfo

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.study.StudyInfo
import com.foryouandme.entity.user.User

data class StudyInfoState(
    val user: LazyData<User> = LazyData.Empty,
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val studyInfo: LazyData<StudyInfo> = LazyData.Empty
) {

    companion object {

        fun mock(): StudyInfoState =
            StudyInfoState(
                configuration = Configuration.mock().toData(),
                studyInfo = StudyInfo.mock().toData()
            )

    }

}

sealed class StudyInfoAction {

    object GetConfiguration : StudyInfoAction()
    object GetStudyInfo : StudyInfoAction()
    object GetUser : StudyInfoAction()

}

sealed class StudyInfoEvent {

    data class StudyInfoError(val error: ForYouAndMeException) : StudyInfoEvent()

}
