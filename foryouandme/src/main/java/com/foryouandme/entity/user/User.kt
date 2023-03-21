package com.foryouandme.entity.user

import com.foryouandme.entity.integration.IntegrationApp
import com.foryouandme.entity.permission.Permission
import com.foryouandme.entity.phase.StudyPhase
import com.foryouandme.entity.phase.UserStudyPhase
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset

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
    val points: Int,
    val permissions: List<Permission>,
    val phase: UserStudyPhase?
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
                timeZone = ZoneOffset.UTC,
                points = 30,
                permissions = listOf(Permission.Location, Permission.Camera),
                phase = UserStudyPhase.mock()
            )

    }

}

data class UserCustomData(
    val identifier: String,
    val value: String?,
    val name: String,
    val type: UserCustomDataType,
    val phase: StudyPhase?
) {

    companion object {
        const val PREGNANCY_END_DATE_IDENTIFIER: String = "1"
        const val DELIVERY_DATE_IDENTIFIER: String = "2"
    }

}

data class UserCustomDataItem(
    val identifier: String,
    val value: String
)

sealed class UserCustomDataType {

    object String : UserCustomDataType()
    object Date : UserCustomDataType()
    data class Items(val items: List<UserCustomDataItem>) : UserCustomDataType()

}