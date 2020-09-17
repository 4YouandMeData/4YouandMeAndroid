package org.fouryouandme.studyinfo

import org.fouryouandme.core.entity.configuration.Configuration

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
