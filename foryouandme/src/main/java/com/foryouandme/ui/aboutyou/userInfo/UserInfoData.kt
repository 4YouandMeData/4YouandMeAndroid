package com.foryouandme.ui.aboutyou.userInfo

import com.foryouandme.core.arch.LazyData
import com.foryouandme.domain.error.ForYouAndMeException
import com.foryouandme.entity.configuration.Configuration
import com.foryouandme.entity.user.User
import com.foryouandme.ui.aboutyou.userInfo.compose.EntryItem
import org.threeten.bp.LocalDate

data class UserInfoState(
    val configuration: LazyData<Configuration> = LazyData.Empty,
    val user: LazyData<User> = LazyData.Empty,
    val entries: List<EntryItem> = emptyList(),
    val isEditing: Boolean = false,
    val upload: LazyData<Unit> = LazyData.Empty,
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
    object Upload: UserInfoAction()

}

sealed class UserInfoEvent {

    object UploadCompleted : UserInfoEvent()
    data class UploadError(val error: ForYouAndMeException) : UserInfoEvent()

}

