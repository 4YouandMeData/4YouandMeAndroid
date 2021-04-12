package com.foryouandme.ui.aboutyou.dailysurveytime

import com.foryouandme.entity.usersettings.UserSettings

data class AboutYouDailySurveyTimeState(
    val userSettings: UserSettings? = null
)

sealed class AboutYouDailySurveyTimeStateUpdate {
    object GetUserSettings : AboutYouDailySurveyTimeStateUpdate()
}

sealed class AboutYouDailySurveyTimeLoading {
    object GetUserSettings : AboutYouDailySurveyTimeLoading()
}

sealed class AboutYouDailySurveyTimeError {
    object GetUserSettings : AboutYouDailySurveyTimeError()
}

sealed class AboutYouDailySurveyTimeStateEvent {
    object GetUserSettings : AboutYouDailySurveyTimeStateEvent()
}