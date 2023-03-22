package com.foryouandme.ui.userInfo

import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.navigation.NavigationAction
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.ui.userInfo.compose.EntryItem
import org.threeten.bp.LocalDate

data class UserInfoState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val user: LazyData<User> = LazyData.Empty,
    val entries: List<EntryItem> = emptyList(),
    val isEditing: Boolean = false,
    val upload: LazyData<Unit> = LazyData.Empty,
    val phaseAlert: Boolean = false,
    val pendingPhaseSwitch: Pair<List<UserCustomData>, StudyPhase>? = null
)

sealed class UserInfoAction {

    object GetConfiguration : UserInfoAction()
    object GetUser : UserInfoAction()
    object ToggleEditMode : UserInfoAction()
    data class OnTextChanged(val item: EntryItem.Text, val text: String) : UserInfoAction()
    data class OnDateChanged(val item: EntryItem.Date, val date: LocalDate) : UserInfoAction()
    data class OnPickerChanged(
        val item: EntryItem.Picker,
        val value: EntryItem.Picker.Value
    ) : UserInfoAction()

    object Upload : UserInfoAction()
    object PhaseSwitch : UserInfoAction()
    object AbortPhaseSwitch : UserInfoAction()
}

sealed class UserInfoEvent {

    object UploadCompleted : UserInfoEvent()
    data class UploadError(val error: ForYouAndMeException) : UserInfoEvent()

}


object UserInfoToStudyInfoDetail : NavigationAction

