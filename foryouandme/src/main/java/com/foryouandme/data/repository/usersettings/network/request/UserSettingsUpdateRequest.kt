package com.foryouandme.data.repository.usersettings.network.request

import com.foryouandme.entity.usersettings.UserSettings
import com.squareup.moshi.Json

data class UserSettingsUpdateRequest (
    @Json(name = "user_setting") val userSetting: UserSettingsUpdateDataRequest
) {

    companion object {

        fun build(userSettings: UserSettings) : UserSettingsUpdateRequest =
            UserSettingsUpdateRequest(
                UserSettingsUpdateDataRequest(
                    userSettings.dailySurveyTime.toSecondOfDay().toLong()
                )
            )

    }

}

data class UserSettingsUpdateDataRequest(
    @Json(name = "daily_survey_time_seconds_since_midnight")
    val dailySurveyTimeSecondsSinceMidnight: Long
)