package org.fouryouandme.htmldetails

import org.fouryouandme.core.entity.studyinfo.StudyInfo


data class HtmlDetailsState(
    val studyInfo: StudyInfo
)

sealed class HtmlDetailsStateUpdate {
    data class Initialization(val studyInfo: StudyInfo) : HtmlDetailsStateUpdate()
}

sealed class HtmlDetailsLoading {
    object Initialization : HtmlDetailsLoading()
}

sealed class HtmlDetailsError {
    object Initialization : HtmlDetailsError()
}