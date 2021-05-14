package com.foryouandme.ui.dailysurveytime

import com.foryouandme.entity.usersettings.UserSettings
import org.threeten.bp.LocalTime

data class DailySurveyTimeState(
    val userSettings: UserSettings? = null
)

sealed class DailySurveyTimeStateUpdate {
    object GetUserSettings : DailySurveyTimeStateUpdate()
    object SaveUserSettings : DailySurveyTimeStateUpdate()
}

sealed class DailySurveyTimeLoading {
    object GetUserSettings : DailySurveyTimeLoading()
    object SaveUserSettings : DailySurveyTimeLoading()
}

sealed class DailySurveyTimeError {
    object GetUserSettings : DailySurveyTimeError()
    object SaveUserSettings : DailySurveyTimeError()
}

sealed class DailySurveyTimeStateEvent {
    object GetUserSettings : DailySurveyTimeStateEvent()
    data class SaveUserSettings(
        val dailySurveyTime: LocalTime
    ) : DailySurveyTimeStateEvent()
}