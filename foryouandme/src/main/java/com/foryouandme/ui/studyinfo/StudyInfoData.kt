package com.foryouandme.ui.studyinfo

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.entity.configuration.Configuration

data class StudyInfoState(
    val configuration: LazyData<Configuration> = LazyData.Empty
) {

    companion object {

        fun mock(): StudyInfoState =
            StudyInfoState(configuration = Configuration.mock().toData())

    }

}

sealed class StudyInfoAction {

    object GetConfiguration : StudyInfoAction()

}
