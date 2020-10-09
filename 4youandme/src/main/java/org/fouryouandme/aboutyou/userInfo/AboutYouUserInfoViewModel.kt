package org.fouryouandme.aboutyou.userInfo

import arrow.core.Either
import arrow.fx.ForIO
import com.giacomoparisi.recyclerdroid.core.DroidItem
import org.fouryouandme.core.arch.android.BaseViewModel
import org.fouryouandme.core.arch.android.Empty
import org.fouryouandme.core.arch.deps.ImageConfiguration
import org.fouryouandme.core.arch.deps.Runtime
import org.fouryouandme.core.arch.navigation.Navigator
import org.fouryouandme.core.entity.configuration.Configuration
import org.fouryouandme.core.entity.user.User
import org.fouryouandme.core.entity.user.UserCustomDataType
import org.threeten.bp.Instant
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class AboutYouUserInfoViewModel(
    navigator: Navigator,
    runtime: Runtime<ForIO>
) : BaseViewModel<
        ForIO,
        AboutYouUserInfoState,
        AboutYouUserInfoStateUpdate,
        AboutYouUserInfoError,
        Empty>
    (navigator = navigator, runtime = runtime) {

    suspend fun initialize(
        configuration: Configuration,
        imageConfiguration: ImageConfiguration,
        user: User
    ) {

        val items =
            user.customData.mapNotNull {

                when (it.type) {
                    UserCustomDataType.String ->
                        EntryStringItem(
                            it.identifier,
                            configuration,
                            imageConfiguration,
                            it.name,
                            it.value.orEmpty(),
                            false
                        )
                    UserCustomDataType.Date ->
                        EntryDateItem(
                            it.identifier,
                            configuration,
                            imageConfiguration,
                            it.name,
                            Either.catch { Instant.parse(it.value).atZone(ZoneOffset.UTC) }
                                .orNull(),
                            false
                        )
                    is UserCustomDataType.Items -> null
                }

            }

        setStateFx(AboutYouUserInfoState(items, false))
        { AboutYouUserInfoStateUpdate.Initialization(it.items) }

    }

    /* --- edit --- */

    suspend fun toggleEdit(): Unit {

        if (state().isEditing) {

            // TODO: save data

            setStateFx(
                state().copy(isEditing = false, items = setItemsEditMode(false))
            ) { AboutYouUserInfoStateUpdate.EditMode(it.isEditing, it.items) }

        } else {

            setStateFx(
                state().copy(isEditing = true, items = setItemsEditMode(true))
            ) { AboutYouUserInfoStateUpdate.EditMode(it.isEditing, it.items) }

        }

    }

    private suspend fun setItemsEditMode(isEditable: Boolean): List<DroidItem<Any>> =
        state().items.map {

            when (it) {
                is EntryStringItem -> it.copy(isEditable = isEditable)
                is EntryDateItem -> it.copy(isEditable = isEditable)
                else -> it
            }

        }


    /* --- update --- */

    suspend fun updateDateItem(id: String, date: ZonedDateTime): Unit {

        val items =
            state().items.map {

                when (it) {
                    is EntryDateItem -> if (it.id == id) it.copy(value = date) else it
                    else -> it
                }

            }

        setStateFx(state().copy(items = items))
        { AboutYouUserInfoStateUpdate.Items(items) }

    }

}