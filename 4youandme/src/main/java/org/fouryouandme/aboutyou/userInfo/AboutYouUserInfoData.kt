package org.fouryouandme.aboutyou.userInfo

import com.giacomoparisi.recyclerdroid.core.DroidItem

data class AboutYouUserInfoState(
    val items: List<DroidItem<Any>>,
    val isEditing: Boolean
)

sealed class AboutYouUserInfoStateUpdate {

    data class Initialization(val items: List<DroidItem<Any>>) : AboutYouUserInfoStateUpdate()

    data class EditMode(
        val isEditing: Boolean,
        val items: List<DroidItem<Any>>
    ) : AboutYouUserInfoStateUpdate()

    data class Items(val items: List<DroidItem<Any>>) : AboutYouUserInfoStateUpdate()

}

sealed class AboutYouUserInfoError {

    object Initialization : AboutYouUserInfoError()

}

