package com.foryouandme.ui.dailysurveytime

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.toData
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.usersettings.UserSettings
import org.threeten.bp.LocalTime

data class DailySurveyTimeState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val userSettings: LazyData<UserSettings> = LazyData.Empty,
    val save: LazyData<Unit> = LazyData.Empty
) {

    companion object {

        fun mock(): DailySurveyTimeState =
            DailySurveyTimeState(
                configuration = Configuration.mock().toData(),
                userSettings = UserSettings.mock().toData(),
            )

    }

}

sealed class DailySurveyTimeAction {
    object GetConfiguration: DailySurveyTimeAction()
    object GetUserSettings : DailySurveyTimeAction()
    data class UpdateTime(val time: LocalTime): DailySurveyTimeAction()
    object SaveUserSettings : DailySurveyTimeAction()
}

sealed class DailySurveyTimeEvent {
    object Saved : DailySurveyTimeEvent()
    data class SaveError(val error: ForYouAndMeException) : DailySurveyTimeEvent()
}