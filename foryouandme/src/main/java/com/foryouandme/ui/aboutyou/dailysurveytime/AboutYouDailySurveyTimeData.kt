package com.foryouandme.ui.aboutyou.dailysurveytime

import com.foryouandme.entity.usersettings.UserSettings
import org.threeten.bp.LocalTime

data class AboutYouDailySurveyTimeState(
    val userSettings: UserSettings? = null
)

sealed class AboutYouDailySurveyTimeStateUpdate {
    object GetUserSettings : AboutYouDailySurveyTimeStateUpdate()
    object SaveUserSettings : AboutYouDailySurveyTimeStateUpdate()
}

sealed class AboutYouDailySurveyTimeLoading {
    object GetUserSettings : AboutYouDailySurveyTimeLoading()
    object SaveUserSettings : AboutYouDailySurveyTimeLoading()
}

sealed class AboutYouDailySurveyTimeError {
    object GetUserSettings : AboutYouDailySurveyTimeError()
    object SaveUserSettings : AboutYouDailySurveyTimeError()
}

sealed class AboutYouDailySurveyTimeStateEvent {
    object GetUserSettings : AboutYouDailySurveyTimeStateEvent()
    data class SaveUserSettings(
        val dailySurveyTime: LocalTime
    ) : AboutYouDailySurveyTimeStateEvent()
}