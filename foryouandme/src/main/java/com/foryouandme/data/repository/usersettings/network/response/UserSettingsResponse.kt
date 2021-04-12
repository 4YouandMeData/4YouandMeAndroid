package com.foryouandme.data.repository.usersettings.network.response

import com.foryouandme.entity.usersettings.UserSettings
import com.squareup.moshi.Json
import moe.banana.jsonapi2.JsonApi
import moe.banana.jsonapi2.Resource

@JsonApi(type = "user_setting")
data class UserSettingsResponse(
    @field:Json(name = "daily_survey_time_seconds_since_midnight") val dailySurveyTimeSecondsSinceMidnight: Long? = null
) : Resource() {

    fun toUserSettings(): UserSettings? {

        return when (null) {
            dailySurveyTimeSecondsSinceMidnight -> null
            else -> UserSettings(dailySurveyTimeSecondsSinceMidnight)
        }

    }

}