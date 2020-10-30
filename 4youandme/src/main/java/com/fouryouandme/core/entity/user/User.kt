package com.fouryouandme.core.entity.user

import com.fouryouandme.core.ext.StudyIntegration
import org.threeten.bp.ZoneId

data class User(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val daysInStudy: Int,
    val identities: List<StudyIntegration>,
    val onBoardingCompleted: Boolean,
    val token: String,
    val customData: List<UserCustomData>,
    val timeZone: ZoneId?,
    val points: Int
) {

    fun getCustomDataByIdentifier(identifier: String): UserCustomData? =
        customData.firstOrNull { it.identifier == identifier }

}

data class UserCustomData(
    val identifier: String,
    val value: String?,
    val name: String,
    val type: UserCustomDataType,
)

const val PREGNANCY_END_DATE_IDENTIFIER: String = "1"
const val BABY_GENDER_IDENTIFIER: String = "2"
const val BABY_NAME_IDENTIFIER: String = "3"

data class UserCustomDataItem(
    val identifier: String,
    val value: String
)

sealed class UserCustomDataType {

    object String : UserCustomDataType()
    object Date : UserCustomDataType()
    data class Items(val items: List<UserCustomDataItem>) : UserCustomDataType()

}