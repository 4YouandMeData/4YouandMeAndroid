package com.foryouandme.entity.user

import com.foryouandme.entity.integration.IntegrationApp
import org.threeten.bp.ZoneId

data class User(
    val id: String,
    val email: String?,
    val phoneNumber: String?,
    val daysInStudy: Int,
    val identities: List<IntegrationApp>,
    val onBoardingCompleted: Boolean,
    val token: String,
    val customData: List<UserCustomData>,
    val timeZone: ZoneId?,
    val points: Int
) {

    fun getCustomDataByIdentifier(identifier: String): UserCustomData? =
        customData.firstOrNull { it.identifier == identifier }

    companion object {

        fun mock(): User =
            User(
                id = "id",
                email = "email@email.com",
                phoneNumber = "phone_number",
                daysInStudy = 30,
                identities = listOf(IntegrationApp.Oura, IntegrationApp.Fitbit),
                onBoardingCompleted = true,
                token = "token",
                customData = emptyList(),
                timeZone = ZoneId.of("Europe/Paris"),
                points = 30
            )

    }

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