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
import com.foryouandme.domain.usecase.phase.SwitchStudyPhaseUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.domain.usecase.user.UpdateUserCustomDataUseCase
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import com.foryouandme.entity.user.UserCustomDataItem
import com.foryouandme.entity.user.UserCustomDataType
import com.foryouandme.ui.compose.error.toForYouAndMeException
import com.foryouandme.ui.userInfo.compose.EntryItem
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
    private val switchStudyPhaseUseCase: SwitchStudyPhaseUseCase,
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
                        description = getEntryDescription(data.identifier),
                        value = catchToNull { LocalDate.parse(data.value) },
                        isEditable = canEditField(data.identifier, data.value)
                    )
                is UserCustomDataType.Items ->
                    EntryItem.Picker(
                        id = data.identifier,
                        name = data.name,
                        description = getEntryDescription(data.identifier),
                        value =
                        data.type.items
                            .firstOrNull { it.identifier == data.value }
                            ?.let { EntryItem.Picker.Value(it.identifier, it.value) },
                        values =
                        data.type.items.map { EntryItem.Picker.Value(it.identifier, it.value) },
                    )
            }

        }

    private fun getEntryDescription(entryId: String): String? {
        val configuration = state.value.configuration.currentOrPrevious()
        return if (configuration != null)
            when (entryId) {
                UserCustomData.PREGNANCY_END_DATE_IDENTIFIER ->
                    configuration.text.studyInfo.dueDateExtra
                UserCustomData.DELIVERY_DATE_IDENTIFIER ->
                    configuration.text.studyInfo.deliveryDateExtra
                else -> null
            }
        else null
    }

    /* --- edit --- */

    private fun canEditField(id: String, value: String?): Boolean =
        if (id == UserCustomData.DELIVERY_DATE_IDENTIFIER) value == null
        else true

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
                if (it == item)
                    item.copy(value = date, isEditable = canEditField(item.id, date.toString()))
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
                                    UserCustomDataType.String,
                                    getCustomDataPhase(item.id)
                                )
                            is EntryItem.Date ->
                                UserCustomData(
                                    item.id,
                                    item.value?.format(DateTimeFormatter.ISO_LOCAL_DATE),
                                    item.name,
                                    UserCustomDataType.Date,
                                    getCustomDataPhase(item.id)
                                )
                            is EntryItem.Picker ->
                                UserCustomData(
                                    item.id,
                                    item.value?.id,
                                    item.name,
                                    UserCustomDataType.Items(
                                        item.values.map { UserCustomDataItem(it.id, it.name) }
                                    ),
                                    getCustomDataPhase(item.id)
                                )
                        }
                    }

                val currentCustomData =
                    state.value.user.currentOrPrevious()?.customData ?: emptyList()
                val diff =
                    data.mapNotNull { update ->
                        val current = currentCustomData.find { it.identifier == update.identifier }
                        if (update.value != current?.value) update
                        else null
                    }

                diff.forEach {
                    val switch = it.phase
                    if (switch != null) switchStudyPhaseUseCase(switch)
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

    private fun getCustomDataPhase(id: String): StudyPhase? =
        state.value.user.dataOrNull()?.customData?.find { it.identifier == id }?.phase

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