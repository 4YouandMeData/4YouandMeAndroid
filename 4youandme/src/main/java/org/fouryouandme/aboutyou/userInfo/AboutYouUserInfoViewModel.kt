package org.fouryouandme.aboutyou.userInfo

import arrow.core.Either
import arrow.core.flatMap
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.modules.AuthModule
import org.fouryouandme.core.arch.error.FourYouAndMeError
import org.fouryouandme.core.arch.error.handleAuthError
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.arch.navigation.RootNavController
import org.fouryouandme.core.cases.CachePolicy
import org.fouryouandme.core.cases.auth.AuthUseCase.getUser
import org.fouryouandme.core.cases.auth.AuthUseCase.updateUser
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.entity.user.UserCustomData
import org.fouryouandme.core.entity.user.UserCustomDataItem
import org.fouryouandme.core.entity.user.UserCustomDataType
import org.fouryouandme.core.ext.emptyOrBlankToNull
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
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
                            Either.catch { Instant.parse(data.value).atZone(ZoneOffset.UTC) }
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

    suspend fun updateDateItem(id: String, date: ZonedDateTime): Unit {

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


    private suspend fun upload(rootNavController: RootNavController): Either<FourYouAndMeError, Unit> {

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
                            item.value?.format(DateTimeFormatter.ISO_INSTANT),
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