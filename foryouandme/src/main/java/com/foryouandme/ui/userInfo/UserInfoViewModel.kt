package com.foryouandme.ui.userInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foryouandme.core.arch.LazyData
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.toData
import com.foryouandme.core.arch.toError
import com.foryouandme.core.ext.*
import com.foryouandme.domain.policy.Policy
import com.foryouandme.domain.usecase.configuration.GetConfigurationUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.domain.usecase.user.UpdateUserCustomDataUseCase
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataItem
import com.foryouandme.entity.user.UserCustomDataType
import com.foryouandme.ui.userInfo.compose.EntryItem
import com.foryouandme.ui.compose.error.toForYouAndMeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val updateUserCustomDataUseCase: UpdateUserCustomDataUseCase,
    val imageConfiguration: ImageConfiguration
) : ViewModel() {

    /* --- state --- */

    private val state = MutableStateFlow(UserInfoState())
    val stateFlow = state as StateFlow<UserInfoState>

    private val eventChannel = Channel<UserInfoEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        execute(UserInfoAction.GetConfiguration)
    }

    /* --- configuration --- */

    private fun getConfiguration(): Action =
        action(
            {
                state.emit(state.value.copy(configuration = state.value.configuration.toLoading()))
                val configuration = getConfigurationUseCase(Policy.LocalFirst)
                state.emit(state.value.copy(configuration = configuration.toData()))
                execute(UserInfoAction.GetUser)
            },
            { state.emit(state.value.copy(configuration = it.toError())) }
        )

    /* --- user --- */

    private fun getUser(): Action =
        action(
            {
                val configuration = state.value.configuration.dataOrNull()

                if (configuration != null) {
                    state.emit(state.value.copy(user = state.value.user.toLoading()))
                    val user = getUserUseCase(Policy.LocalFirst)
                    val entries = getEntryItems(user)
                    state.emit(state.value.copy(user = user.toData(), entries = entries))
                } else execute(UserInfoAction.GetConfiguration)

            },
            { state.emit(state.value.copy(user = it.toError())) }
        )

    private fun getEntryItems(user: User): List<EntryItem> =
        user.customData.map { data ->

            when (data.type) {
                UserCustomDataType.String ->
                    EntryItem.Text(
                        id = data.identifier,
                        name = data.name,
                        value = data.value.orEmpty()
                    )
                UserCustomDataType.Date ->
                    EntryItem.Date(
                        id = data.identifier,
                        name = data.name,
                        value = catchToNull { LocalDate.parse(data.value) },
                    )
                is UserCustomDataType.Items ->
                    EntryItem.Picker(
                        id = data.identifier,
                        name = data.name,
                        value =
                        data.type.items
                            .firstOrNull { it.identifier == data.value }
                            ?.let { EntryItem.Picker.Value(it.identifier, it.value) },
                        values =
                        data.type.items.map { EntryItem.Picker.Value(it.identifier, it.value) },
                    )
            }

        }

    /* --- edit --- */

    private suspend fun toggleEdit() {

        if (state.value.isEditing) {
            execute(UserInfoAction.Upload)
            state.emit(state.value.copy(isEditing = false))
        } else
            state.emit(state.value.copy(isEditing = true))

    }

    /* --- update --- */

    private suspend fun updateEntryText(item: EntryItem.Text, text: String) {

        val entries =
            state.value.entries.map {
                if (it == item) item.copy(value = text)
                else it
            }

        state.emit(state.value.copy(entries = entries))

    }

    private suspend fun updateEntryDate(item: EntryItem.Date, date: LocalDate) {

        val entries =
            state.value.entries.map {
                if (it == item) item.copy(value = date)
                else it
            }

        state.emit(state.value.copy(entries = entries))

    }

    private suspend fun updateEntryPicker(item: EntryItem.Picker, value: EntryItem.Picker.Value) {

        val entries =
            state.value.entries.map {
                if (it == item) item.copy(value = value)
                else it
            }

        state.emit(state.value.copy(entries = entries))

    }

    /* --- upload --- */

    private fun upload(): Action =
        action(
            {
                state.emit(state.value.copy(upload = state.value.upload.toLoading()))
                val data =
                    state.value.entries.map { item ->
                        when (item) {
                            is EntryItem.Text ->
                                UserCustomData(
                                    item.id,
                                    item.value.emptyOrBlankToNull(),
                                    item.name,
                                    UserCustomDataType.String
                                )
                            is EntryItem.Date ->
                                UserCustomData(
                                    item.id,
                                    item.value?.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                    item.name,
                                    UserCustomDataType.Date
                                )
                            is EntryItem.Picker ->
                                UserCustomData(
                                    item.id,
                                    item.value?.id,
                                    item.name,
                                    UserCustomDataType.Items(
                                        item.values.map { UserCustomDataItem(it.id, it.name) }
                                    )
                                )
                        }
                    }

                updateUserCustomDataUseCase(data)
                state.emit(state.value.copy(upload = LazyData.unit()))
                eventChannel.send(UserInfoEvent.UploadCompleted)

            },
            {
                state.emit(state.value.copy(upload = it.toError()))
                eventChannel.send(UserInfoEvent.UploadError(it.toForYouAndMeException()))
            }
        )

    /* --- action --- */

    fun execute(action: UserInfoAction) {
        when (action) {
            UserInfoAction.GetConfiguration ->
                viewModelScope.launchAction(getConfiguration())
            UserInfoAction.GetUser ->
                viewModelScope.launchAction(getUser())
            UserInfoAction.ToggleEditMode ->
                viewModelScope.launchSafe { toggleEdit() }
            is UserInfoAction.OnTextChanged ->
                viewModelScope.launchSafe { updateEntryText(action.item, action.text) }
            is UserInfoAction.OnDateChanged ->
                viewModelScope.launchSafe { updateEntryDate(action.item, action.date) }
            is UserInfoAction.OnPickerChanged ->
                viewModelScope.launchSafe { updateEntryPicker(action.item, action.value) }
            UserInfoAction.Upload ->
                viewModelScope.launchAction(upload())
        }
    }

}