package com.foryouandme.entity.usersettings

import com.foryouandme.entity.mock.Mock
import org.threeten.bp.LocalTime

data class UserSettings(
    val dailySurveyTime: LocalTime
) {

    companion object {

        fun mock(): UserSettings =
            UserSettings(dailySurveyTime = Mock.localTime)

    }

}