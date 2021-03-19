package com.foryouandme.domain.usecase.auth

import com.foryouandme.data.datasource.Environment
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.domain.usecase.analytics.SendAnalyticsEventUseCase
import com.foryouandme.domain.usecase.analytics.SetAnalyticsUserIdUseCase
import com.foryouandme.domain.usecase.push.GetPushTokenUseCase
import com.foryouandme.domain.usecase.user.GetUserUseCase
import com.foryouandme.domain.usecase.user.SaveUserUseCase
import com.foryouandme.domain.usecase.user.UpdateUserFirebaseTokenUseCase
import com.foryouandme.domain.usecase.user.UpdateUserTimeZoneUseCase
import com.foryouandme.entity.user.User
import org.threeten.bp.ZoneId
import javax.inject.Inject

class PhoneLoginUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val environment: Environment,
    private val updateUserTimeZoneUseCase: UpdateUserTimeZoneUseCase,
    private val getPushTokenUseCase: GetPushTokenUseCase,
    private val updateUserFirebaseTokenUseCase: UpdateUserFirebaseTokenUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendAnalyticsEventUseCase: SendAnalyticsEventUseCase,
    private val setAnalyticsUserIdUseCase: SetAnalyticsUserIdUseCase,
    private val logOutUseCase: LogOutUseCase
) {

    suspend operator fun invoke(phone: String, code: String, countryCode: String): User =

        try {

            val user = repository.phoneLogin(environment.studyId(), phone, code)!!
            updateUserTimeZoneUseCase(user.token, ZoneId.systemDefault())
            val firebaseToken = getPushTokenUseCase()
            updateUserFirebaseTokenUseCase(user.token, firebaseToken)
            saveUserUseCase(user)

            val updatedUser = getUserUseCase()
            sendAnalyticsEventUseCase(
                AnalyticsEvent.UserRegistration(countryCode),
                EAnalyticsProvider.ALL
            )
            setAnalyticsUserIdUseCase(updatedUser.id)

            updatedUser

        } catch (throwable: Throwable) {
            logOutUseCase()
            throw throwable
        }

}