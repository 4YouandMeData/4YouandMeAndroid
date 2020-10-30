package com.foryouandme.aboutyou.userInfo

import arrow.core.Either
import arrow.core.flatMap
import com.foryouandme.core.arch.android.BaseViewModel
import com.foryouandme.core.arch.deps.ImageConfiguration
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.arch.error.handleAuthError
import com.foryouandme.core.arch.navigation.Navigator
import com.foryouandme.core.arch.navigation.RootNavController
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.auth.AuthUseCase.getUser
import com.foryouandme.core.cases.auth.AuthUseCase.updateUser
import com.foryouandme.core.entity.configuration.Configuration
import com.foryouandme.core.entity.user.User
import com.foryouandme.core.entity.user.UserCustomData
import com.foryouandme.core.entity.user.UserCustomDataItem
import com.foryouandme.core.entity.user.UserCustomDataType
import com.foryouandme.core.ext.emptyOrBlankToNull
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class AboutYouUserInfoViewModel(
    navigator: Navigator,
    private val authModule: AuthModule
) : BaseViewModel<
        AboutYouUserInfoState,
        AboutYouUserInfoStateUpdate,
        AboutYouUserInfoError,
        AboutYouUserInfoLoading>
    (navigator = navigator) {

    suspend fun initialize(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        user: User
    ) {

        val items =
            user.customData.map { data ->

                when (data.type) {
                    UserCustomDataType.String ->
                        EntryTextItem(
                            data.identifier,
                            configuration,
                            imageConfiguration,
                            data.name,
                            data.value.orEmpty(),
                            false
                        )
                    UserCustomDataType.Date ->
                        EntryDateItem(
                            data.identifier,
                            configuration,
                            imageConfiguration,
                            data.name,
                            Either.catch { LocalDate.parse(data.value) }
                                .orNull(),
                            false
                        )
                    is UserCustomDataType.Items ->
                        EntryPickerItem(
                            data.identifier,
                            configuration,
                            imageConfiguration,
                            data.name,
                            data.type.items
                                .firstOrNull { it.identifier == data.value }
                                ?.let { EntryPickerValue(it.identifier, it.value) },
                            data.type.items.map { EntryPickerValue(it.identifier, it.value) },
                            false
                        )
                }

            }

        setState(AboutYouUserInfoState(items, false))
        { AboutYouUserInfoStateUpdate.Initialization(it.items) }

    }

    /* --- edit --- */

    suspend fun toggleEdit(rootNavController: RootNavController): Unit {

        if (state().isEditing) {

            upload(rootNavController)
                .fold(
                    { setError(it, AboutYouUserInfoError.Upload) },
                    {
                        setState(
                            state().copy(isEditing = false, items = setItemsEditMode(false))
                        ) { AboutYouUserInfoStateUpdate.EditMode(it.isEditing, it.items) }
                    }
                )
        } else {

            setState(
                state().copy(isEditing = true, items = setItemsEditMode(true))
            ) { AboutYouUserInfoStateUpdate.EditMode(it.isEditing, it.items) }

        }

    }

    private suspend fun setItemsEditMode(isEditable: Boolean): List<DroidItem<Any>> =
        state().items.map {

            when (it) {
                is EntryTextItem -> it.copy(isEditable = isEditable)
                is EntryDateItem -> it.copy(isEditable = isEditable)
                is EntryPickerItem -> it.copy(isEditable = isEditable)
                else -> it
            }

        }


    /* --- update --- */

    suspend fun updateTextItem(id: String, text: String): Unit {

        val items =
            state().items.map {

                when (it) {
                    is EntryTextItem -> if (it.id == id) it.copy(value = text) else it
                    else -> it
                }

            }

        setState(state().copy(items = items))
        { AboutYouUserInfoStateUpdate.Items(items) }

    }

    suspend fun updateDateItem(id: String, date: LocalDate): Unit {

        val items =
            state().items.map {

                when (it) {
                    is EntryDateItem -> if (it.id == id) it.copy(value = date) else it
                    else -> it
                }

            }

        setState(state().copy(items = items))
        { AboutYouUserInfoStateUpdate.Items(items) }

    }

    suspend fun updatePickerItem(id: String, value: EntryPickerValue): Unit {

        val items =
            state().items.map {

                when (it) {
                    is EntryPickerItem ->
                        if (it.id == id) it.copy(value = value)
                        else it
                    else -> it
                }

            }

        setState(state().copy(items = items))
        { AboutYouUserInfoStateUpdate.Items(items) }

    }

    /* --- upload --- */


    private suspend fun upload(rootNavController: RootNavController): Either<ForYouAndMeError, Unit> {

        showLoading(AboutYouUserInfoLoading.Upload)

        val data =
            state().items.mapNotNull { item ->

                when (item) {
                    is EntryTextItem ->
                        UserCustomData(
                            item.id,
                            item.value.emptyOrBlankToNull(),
                            item.name,
                            UserCustomDataType.String
                        )
                    is EntryDateItem ->
                        UserCustomData(
                            item.id,
                            item.value?.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            item.name,
                            UserCustomDataType.Date
                        )
                    is EntryPickerItem ->
                        UserCustomData(
                            item.id,
                            item.value?.id,
                            item.name,
                            UserCustomDataType.Items(
                                item.items.map { UserCustomDataItem(it.id, it.name) }
                            )
                        )
                    else -> null

                }

            }

        val upload =
            authModule.updateUser(data)
                .flatMap { authModule.getUser(CachePolicy.Network) }
                .map { Unit }
                .handleAuthError(rootNavController, navigator)

        hideLoading(AboutYouUserInfoLoading.Upload)

        return upload

    }


}