package com.foryouandme.ui.aboutyou.dailysurveytime

data class AboutYouDailySurveyTimeState(
    val dailySurveyTime: String
)

sealed class AboutYouDailySurveyTimeStateUpdate {

    data class Initialization(
        val dailySurveyTime: String
    ) : AboutYouDailySurveyTimeStateUpdate()

}

sealed class AboutYouDailySurveyTimeLoading {

    object Initialization : AboutYouDailySurveyTimeLoading()
    object Upload: AboutYouDailySurveyTimeLoading()

}

sealed class AboutYouDailySurveyTimeError {
    object Initialization: AboutYouDailySurveyTimeError()
}