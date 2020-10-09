package org.fouryouandme.core.entity.user

import org.fouryouandme.core.ext.StudyIntegration

data class User(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val daysInStudy: Int,
    val identities: List<StudyIntegration>,
    val onBoardingCompleted: Boolean,
    val token: String,
    val customData: List<UserCustomData>
)

data class UserCustomData(
    val identifier: String,
    val value: String?,
    val name: String,
    val type: UserCustomDataType,
)

data class UserCustomDataItem(
    val identifier: String,
    val value: String
)

sealed class UserCustomDataType {

    object String : UserCustomDataType()
    object Date : UserCustomDataType()
    data class Items(val items: List<UserCustomDataItem>) : UserCustomDataType()

}