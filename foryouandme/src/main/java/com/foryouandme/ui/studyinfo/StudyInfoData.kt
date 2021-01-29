package com.foryouandme.ui.studyinfo

import com.foryouandme.entity.configuration.Configuration

data class StudyInfoState(
    val configuration: Configuration? = null
)

sealed class StudyInfoStateUpdate {
    data class Initialization(val configuration: Configuration) : StudyInfoStateUpdate()
}

sealed class StudyInfoLoading {
    object Initialization : StudyInfoLoading()
}

sealed class StudyInfoError {
    object Initialization : StudyInfoError()
}

sealed class StudyInfoStateEvent {

    object Initialization : StudyInfoStateEvent()

}
