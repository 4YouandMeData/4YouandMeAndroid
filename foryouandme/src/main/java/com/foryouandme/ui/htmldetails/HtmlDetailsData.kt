package com.foryouandme.ui.htmldetails

import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.studyinfo.StudyInfo


data class HtmlDetailsState(
    val configuration: Configuration? = null,
    val studyInfo: StudyInfo? = null
)

sealed class HtmlDetailsStateUpdate {
    data class Initialization(
        val configuration: Configuration,
        val studyInfo: StudyInfo
    ) : HtmlDetailsStateUpdate()
}

sealed class HtmlDetailsLoading {
    object Initialization : HtmlDetailsLoading()
}

sealed class HtmlDetailsError {
    object Initialization : HtmlDetailsError()
}

enum class EHtmlDetails {

    INFO,
    REWARD,
    FAQ

}

sealed class HtmlDetailsStateEvent {

    object Initialize : HtmlDetailsStateEvent()

}