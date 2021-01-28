package com.foryouandme.core.cases.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.foryouandme.core.arch.deps.modules.AuthModule
import com.foryouandme.core.arch.deps.modules.nullToError
import com.foryouandme.core.arch.error.ForYouAndMeError
import com.foryouandme.core.cases.CachePolicy
import com.foryouandme.core.cases.Memory
import com.foryouandme.domain.usecase.analytics.AnalyticsEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.logEvent
import com.foryouandme.core.cases.analytics.AnalyticsUseCase.setUserId
import com.foryouandme.domain.usecase.analytics.EAnalyticsProvider
import com.foryouandme.core.cases.auth.AuthRepository.fetchUser
import com.foryouandme.core.cases.auth.AuthRepository.loadToken
import com.foryouandme.core.cases.auth.AuthRepository.login
import com.foryouandme.core.cases.auth.AuthRepository.updateFirebaseToken
import com.foryouandme.core.cases.auth.AuthRepository.updateUserCustomData
import com.foryouandme.core.cases.auth.AuthRepository.updateUserTimeZone
import com.foryouandme.core.cases.auth.AuthRepository.verifyPhoneNumber
import com.foryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.foryouandme.entity.user.User
import com.foryouandme.entity.user.UserCustomData
import org.threeten.bp.ZoneId

object AuthUseCase {

    suspend fun AuthModule.verifyPhoneNumber(phone: String): Either<ForYouAndMeError, Unit> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { verifyPhoneNumber(it, phone) }

    suspend fun AuthModule.login(
        phone: String,
        code: String,
        countryCode: String
    ): Either<ForYouAndMeError, User> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { login(it, phone, code) }
            .flatMap { fetchUser(it.token) }
            .nullToError()
            .map {

                analyticsModule.setUserId(it.id)
                //analyticsModule.setUserProperty(UserProperty.DeviceId, deviceId)
                analyticsModule.logEvent(
                    AnalyticsEvent.UserRegistration(countryCode),
                    EAnalyticsProvider.ALL
                )

                it

            }

    internal suspend fun AuthModule.getToken(
        cachePolicy: CachePolicy
    ): Either<ForYouAndMeError, String> =
        when (cachePolicy) {
            CachePolicy.MemoryFirst ->
                Memory.token ?: loadToken()
            CachePolicy.MemoryFirstRefresh ->
                Memory.token ?: loadToken()
            CachePolicy.MemoryOrDisk ->
                Memory.token ?: loadToken()
            CachePolicy.DiskFirst ->
                Memory.token ?: loadToken()
            CachePolicy.DiskFirstRefresh ->
                Memory.token ?: loadToken()
            CachePolicy.Network ->
                Memory.token ?: loadToken()
        }?.right() ?: ForYouAndMeError.UserNotLoggedIn.left()

    internal suspend fun AuthModule.getUser(
        cachePolicy: CachePolicy
    ): Either<ForYouAndMeError, User?> =
        when (cachePolicy) {
            CachePolicy.MemoryFirst ->
                if (Memory.user != null) Memory.user.right()
                else getUser(CachePolicy.Network)
            CachePolicy.MemoryFirstRefresh ->
                if (Memory.user != null) Memory.user.right().also { getUser(CachePolicy.Network) }
                else getUser(CachePolicy.Network)
            CachePolicy.MemoryOrDisk -> Memory.user.right()
            CachePolicy.DiskFirst ->
                if (Memory.user != null) Memory.user.right()
                else getUser(CachePolicy.Network)
            CachePolicy.DiskFirstRefresh ->
                if (Memory.user != null) Memory.user.right().also { getUser(CachePolicy.Network) }
                else getUser(CachePolicy.Network)
            CachePolicy.Network ->
                getToken(CachePolicy.MemoryFirst)
                    .flatMap { fetchUser(it) }
        }

    internal suspend fun AuthModule.isLogged(): Boolean =
        getToken(CachePolicy.MemoryFirst).isRight()

    internal suspend fun AuthModule.updateUser(
        data: List<UserCustomData>
    ): Either<ForYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateUserCustomData(it, data) }

    internal suspend fun AuthModule.updateUserTimeZone(
        zoneId: ZoneId
    ): Either<ForYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateUserTimeZone(it, zoneId) }

    // use only for test
    internal suspend fun AuthModule.resetUserCustomData(): Either<ForYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateUserCustomData(it, emptyList()) }

    internal suspend fun AuthModule.updateFirebaseToken(
        firebaseToken: String
    ): Either<ForYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateFirebaseToken(it, firebaseToken) }

}