package com.foryouandme.data.repository.usersettings.network.request

import com.squareup.moshi.Json

data class UserSettingsUpdateRequest (
    @Json(name = "user_setting") val userSetting: UserSettingsUpdateDataRequest
)

data class UserSettingsUpdateDataRequest(
    @Json(name = "daily_survey_time_seconds_since_midnight") val dailySurveyTimeSecondsSinceMidnight: Long
)