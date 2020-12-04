package com.foryouandme.studyinfo

import com.foryouandme.entity.configuration.Configuration

data class StudyInfoState(
    val configuration: Configuration
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