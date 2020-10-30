package com.fouryouandme.core.cases.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.fouryouandme.core.arch.deps.modules.AuthModule
import com.fouryouandme.core.arch.deps.modules.nullToError
import com.fouryouandme.core.arch.error.FourYouAndMeError
import com.fouryouandme.core.cases.CachePolicy
import com.fouryouandme.core.cases.Memory
import com.fouryouandme.core.cases.auth.AuthRepository.fetchUser
import com.fouryouandme.core.cases.auth.AuthRepository.loadToken
import com.fouryouandme.core.cases.auth.AuthRepository.login
import com.fouryouandme.core.cases.auth.AuthRepository.updateUserCustomData
import com.fouryouandme.core.cases.auth.AuthRepository.verifyPhoneNumber
import com.fouryouandme.core.cases.configuration.ConfigurationUseCase.getConfiguration
import com.fouryouandme.core.entity.user.User
import com.fouryouandme.core.entity.user.UserCustomData

object AuthUseCase {

    suspend fun AuthModule.verifyPhoneNumber(phone: String): Either<FourYouAndMeError, Unit> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { verifyPhoneNumber(it, phone) }

    suspend fun AuthModule.login(
        phone: String,
        code: String
    ): Either<FourYouAndMeError, User> =
        configurationModule.getConfiguration(CachePolicy.MemoryFirst)
            .flatMap { login(it, phone, code) }
            .flatMap { fetchUser(it.token) }
            .nullToError()

    internal suspend fun AuthModule.getToken(
        cachePolicy: CachePolicy
    ): Either<FourYouAndMeError, String> =
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
        }?.right() ?: FourYouAndMeError.UserNotLoggedIn.left()

    internal suspend fun AuthModule.getUser(
        cachePolicy: CachePolicy
    ): Either<FourYouAndMeError, User?> =
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
    ): Either<FourYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateUserCustomData(it, data) }

    // use only for test
    internal suspend fun AuthModule.resetUserCustomData(): Either<FourYouAndMeError, Unit> =
        getToken(CachePolicy.MemoryFirst)
            .flatMap { updateUserCustomData(it, emptyList()) }

}